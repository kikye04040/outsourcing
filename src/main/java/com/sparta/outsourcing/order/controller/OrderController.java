package com.sparta.outsourcing.order.controller;

import com.sparta.outsourcing.order.dto.OrderRequestDto;
import com.sparta.outsourcing.order.dto.OrderResponseDto;
import com.sparta.outsourcing.order.dto.OrderStatusUpdateRequestDto;
import com.sparta.outsourcing.order.entity.Order;
import com.sparta.outsourcing.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{storeId}/order")
    public ResponseEntity<OrderResponseDto> createOrder(@PathVariable Long storeId, @RequestBody OrderRequestDto requestDto) {
        Order order = orderService.createOrder(storeId, requestDto.getMenu(), requestDto.getTotalPrice());
        return ResponseEntity.ok(new OrderResponseDto(order));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusUpdateRequestDto requestDto) {
        orderService.updateOrderStatus(orderId, requestDto.getStatus());
        return ResponseEntity.ok("주문 상태가 성공적으로 업데이트 되었습니다.");
    }
}
