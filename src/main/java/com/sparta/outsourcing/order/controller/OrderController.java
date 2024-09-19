package com.sparta.outsourcing.order.controller;

import com.sparta.outsourcing.order.dto.OrderResponseDto;
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

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestParam Long storeId, @RequestParam Long menuId, @RequestParam int totalPrice) {
        Order order = orderService.createOrder(storeId, menuId, totalPrice);
        return ResponseEntity.ok(order);
    }
}
