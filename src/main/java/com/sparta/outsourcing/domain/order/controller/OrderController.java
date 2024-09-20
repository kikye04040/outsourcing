package com.sparta.outsourcing.domain.order.controller;

import com.sparta.outsourcing.domain.order.dto.OrderRequestDto;
import com.sparta.outsourcing.domain.order.dto.OrderResponseDto;
import com.sparta.outsourcing.domain.order.dto.OrderStatusUpdateRequestDto;
import com.sparta.outsourcing.domain.order.entity.Order;
import com.sparta.outsourcing.domain.order.service.OrderService;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // USER 권한을 가진 사용자가 주문 생성
    @PostMapping("/{storeId}/order")
    public ResponseEntity<OrderResponseDto> createOrder(
            @PathVariable Long storeId,
            @RequestBody OrderRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 사용자 역할 확인
        if (!userDetails.getRole().name().equals("ROLE_USER")) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }

        // 주문 생성
        Order order = orderService.createOrder(storeId, requestDto.getMenu(), requestDto.getTotalPrice());
        return ResponseEntity.ok(new OrderResponseDto(order));
    }

    // OWNER 권한을 가진 사용자가 주문 상태 변경
    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 사용자 역할 확인
        if (!userDetails.getRole().name().equals("ROLE_OWNER")) {
            throw new IllegalArgumentException("인증된 사장님이 아닙니다.");
        }

        // 주문 상태 변경
        orderService.updateOrderStatus(orderId, requestDto.getStatus());
        return ResponseEntity.ok("주문 상태가 성공적으로 업데이트 되었습니다.");
    }
}
