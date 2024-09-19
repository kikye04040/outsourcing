package com.sparta.outsourcing.order.dto;

import com.sparta.outsourcing.order.orderstatusenum.OrderStatus;
import lombok.Getter;

@Getter
public class OrderResponseDto {
    private Long orderId;
    private Long storeId;
    private OrderStatus status;
    private int totalPrice;

    public OrderResponseDto(Long orderId, Long storeId, OrderStatus status, int totalPrice) {
        this.orderId = orderId;
        this.storeId = storeId;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}
