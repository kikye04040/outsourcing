package com.sparta.outsourcing.domain.order.dto;

import com.sparta.outsourcing.domain.order.enums.OrderStatusEnum;
import lombok.Getter;

@Getter
public class OrderStatusUpdateRequestDto {
    private OrderStatusEnum status;

    public OrderStatusUpdateRequestDto(OrderStatusEnum status) {
        this.status = status;
    }
}
