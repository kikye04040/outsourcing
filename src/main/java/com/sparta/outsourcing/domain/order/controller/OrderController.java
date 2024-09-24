package com.sparta.outsourcing.domain.order.controller;

import com.sparta.outsourcing.domain.order.dto.OrderRequestDto;
import com.sparta.outsourcing.domain.order.dto.OrderResponseDto;
import com.sparta.outsourcing.domain.order.dto.OrderStatusUpdateRequestDto;
import com.sparta.outsourcing.domain.order.entity.Order;
import com.sparta.outsourcing.domain.order.service.OrderService;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.exception.BadRequestException;
import com.sparta.outsourcing.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.outsourcing.domain.user.entity.Role.ROLE_USER;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // USER 권한을 가진 사용자가 주문 생성
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestBody OrderRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 사용자 역할 확인을 먼저 수행
        if (!userDetails.getRole().equals(ROLE_USER)) {
            throw new BadRequestException("사용자 계정으로만 주문이 가능합니다.");
        }

        Order order = orderService.createOrder(requestDto, userDetails);

        OrderResponseDto responseDto = new OrderResponseDto(order);
        return ResponseEntity.ok(responseDto);
    }

    // OWNER 권한을 가진 사용자가 주문 상태 변경
    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 사용자 역할 확인
        if (!userDetails.getRole().name().equals("ROLE_OWNER")) {
            throw new ForbiddenException("인증된 사장님이 아닙니다.");
        }

        // 주문 상태 변경
        orderService.updateOrderStatus(orderId, requestDto.getStatus());
        return ResponseEntity.ok("주문 상태가 성공적으로 업데이트 되었습니다.");
    }
}
