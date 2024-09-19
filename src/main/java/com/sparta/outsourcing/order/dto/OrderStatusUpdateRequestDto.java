package com.sparta.outsourcing.order.dto;

import com.sparta.outsourcing.order.enums.OrderStatusEnum;
import lombok.Getter;

@Getter
public class OrderStatusUpdateRequestDto {
    private OrderStatusEnum status;
}
