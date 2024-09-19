package com.sparta.outsourcing.order.dto;

import com.sparta.outsourcing.order.entity.Order;
import com.sparta.outsourcing.order.enums.OrderStatusEnum;
import lombok.Getter;

@Getter
public class OrderResponseDto {
    private Long orderId;
    private OrderStatusEnum status;
    private int totalPrice;
    private LogResponseDto log;

    public OrderResponseDto(Order order) {
        this.orderId = order.getId();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.log = new LogResponseDto(order.getId(), order.getStore().getStoreId());
    }
}
