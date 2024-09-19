package com.sparta.outsourcing.domain.order.dto;

import lombok.Getter;

@Getter
public class OrderRequestDto {
    private String menu;
    private int totalPrice;
}
