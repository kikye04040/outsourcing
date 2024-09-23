package com.sparta.outsourcing.domain.order.controller;

import com.sparta.outsourcing.domain.order.dto.OrderRequestDto;
import com.sparta.outsourcing.domain.order.dto.OrderResponseDto;
import com.sparta.outsourcing.domain.order.dto.OrderStatusUpdateRequestDto;
import com.sparta.outsourcing.domain.order.entity.Order;
import com.sparta.outsourcing.domain.order.enums.OrderStatusEnum;
import com.sparta.outsourcing.domain.order.service.OrderService;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private CustomUserDetails userDetails;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetails = CustomUserDetails.builder()
                .email("test@test.com")
                .name("Test User")
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    void createOrder_Success() {
        // given
        OrderRequestDto requestDto = new OrderRequestDto(1L, List.of(1L, 2L));
        Stores store = new Stores();
        Order mockOrder = new Order(store, new ArrayList<>(), user, 10000);

        when(orderService.createOrder(any(OrderRequestDto.class), any(CustomUserDetails.class)))
                .thenReturn(mockOrder);

        // when
        ResponseEntity<OrderResponseDto> response = orderController.createOrder(requestDto, userDetails);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(orderService, times(1)).createOrder(any(OrderRequestDto.class), any(CustomUserDetails.class));
    }

    @Test
    void createOrder_InvalidUserRole() {
        // given
        CustomUserDetails invalidUser = CustomUserDetails.builder()
                .email("invalid@test.com")
                .name("Invalid User")
                .role(Role.ROLE_OWNER)  // ROLE_USER가 아닌 다른 역할 설정
                .build();

        OrderRequestDto requestDto = new OrderRequestDto(1L, List.of(1L, 2L));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderController.createOrder(requestDto, invalidUser);
        });

        assertEquals("사용자 계정으로만 주문이 가능합니다.", exception.getMessage());

        // orderService 가 호출되지 않았는지 검증
        verify(orderService, never()).createOrder(any(OrderRequestDto.class), any(CustomUserDetails.class));
    }


    @Test
    void updateOrderStatus_Success() {
        // given
        CustomUserDetails ownerDetails = CustomUserDetails.builder()
                .email("owner@test.com")
                .name("Owner User")
                .role(Role.ROLE_OWNER)
                .build();

        OrderStatusUpdateRequestDto requestDto = new OrderStatusUpdateRequestDto(OrderStatusEnum.ACCEPTED);

        // Order 와 Stores 객체 생성 및 설정
        Stores store = new Stores();
        // store.setName("Test Store");

        Order mockOrder = new Order();
        // mockOrder.setStore(store); // Stores 설정

        when(orderService.createOrder(any(OrderRequestDto.class), any(CustomUserDetails.class)))
                .thenReturn(mockOrder);

        // when
        ResponseEntity<String> response = orderController.updateOrderStatus(1L, requestDto, ownerDetails);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("주문 상태가 성공적으로 업데이트 되었습니다.", response.getBody());
        verify(orderService, times(1)).updateOrderStatus(1L, OrderStatusEnum.ACCEPTED);
    }


    @Test
    void updateOrderStatus_InvalidUserRole() {
        // given
        OrderStatusUpdateRequestDto requestDto = new OrderStatusUpdateRequestDto(OrderStatusEnum.COMPLETE);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderController.updateOrderStatus(1L, requestDto, userDetails);
        });

        assertEquals("인증된 사장님이 아닙니다.", exception.getMessage());
        verify(orderService, never()).updateOrderStatus(anyLong(), any(OrderStatusEnum.class));
    }
}
