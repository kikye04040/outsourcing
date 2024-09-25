package com.sparta.outsourcing.domain.order.dto;

import com.sparta.outsourcing.domain.order.enums.OrderStatusEnum;
import lombok.Getter;

@Getter
public class OrderStatusUpdateRequestDto {
    private String status;

    public OrderStatusUpdateRequestDto(String status) {
        this.status = status;
    }

    public OrderStatusUpdateRequestDto() {}
}
