package com.sparta.outsourcing.domain.order.service;

import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.menu.repository.MenuRepository;
import com.sparta.outsourcing.domain.order.dto.OrderRequestDto;
import com.sparta.outsourcing.domain.order.entity.Order;
import com.sparta.outsourcing.domain.order.enums.OrderStatusEnum;
import com.sparta.outsourcing.domain.order.repository.OrderRepository;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.stores.repository.StoresRepository;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.domain.user.entity.User;
import com.sparta.outsourcing.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StoresRepository storesRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    private User mockUser;
    private Stores mockStore;
    private Menu mockMenu1;
    private Menu mockMenu2;
    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 사용자 설정
        mockUser = User.builder()
                .email("test@example.com")
                .name("test")
                .build();

        // 가게 생성 및 ID 설정
        mockStore = new Stores("Test Store", 1, "Category", "Address", "010-1234-5678", "Content", "picture.jpg", 2000, "09:00~22:00", "SUNDAY", mockUser, 1000);
        ReflectionTestUtils.setField(mockStore, "id", 1L); // 가게 ID를 수동으로 설정

        // 메뉴 생성 및 ID 설정
        mockMenu1 = new Menu("Menu1", "Desc1", 5000, mockStore);
        ReflectionTestUtils.setField(mockMenu1, "id", 1L); // 메뉴 1 ID 설정
        mockMenu2 = new Menu("Menu2", "Desc2", 6000, mockStore);
        ReflectionTestUtils.setField(mockMenu2, "id", 2L); // 메뉴 2 ID 설정

        userDetails = mock(CustomUserDetails.class);
        when(userDetails.getEmail()).thenReturn("test@example.com");
        when(userDetails.getRole()).thenReturn(Role.ROLE_USER);
    }

    @Test
    void createOrder() {
        // given
        OrderRequestDto requestDto = new OrderRequestDto(1L, List.of(1L, 2L));

        // 가게, 메뉴, 사용자 리포지토리 모킹
        when(storesRepository.findById(mockStore.getId())).thenReturn(Optional.of(mockStore));
        when(menuRepository.findById(mockMenu1.getId())).thenReturn(Optional.of(mockMenu1));
        when(menuRepository.findById(mockMenu2.getId())).thenReturn(Optional.of(mockMenu2));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(mockUser));

        // 주문 객체 생성
        Order mockOrder = new Order(mockStore, List.of(mockMenu1, mockMenu2), mockUser, 11000);
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        // when
        Order result = orderService.createOrder(requestDto, userDetails);

        // then
        assertEquals(11000, result.getTotalPrice());
        assertEquals(2, result.getMenus().size());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void updateOrderStatusTest() {
        // given
        Long orderId = 1L;
        OrderStatusEnum newStatus = OrderStatusEnum.ACCEPTED;

        // 주문을 찾지 못하는 경우 모킹
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        try {
            orderService.updateOrderStatus(orderId, newStatus);
        } catch (NullPointerException e) {
            assertEquals("해당 주문을 찾을 수 없습니다.", e.getMessage());
        }

        verify(orderRepository, times(1)).findById(orderId);
    }
}