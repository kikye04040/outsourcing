package com.sparta.outsourcing.domain.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDto {
    private List<Long> menuIds;
    private Long storeId;
}
