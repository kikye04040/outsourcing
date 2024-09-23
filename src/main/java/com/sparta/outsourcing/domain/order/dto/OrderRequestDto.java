package com.sparta.outsourcing.domain.order.dto;

import com.sparta.outsourcing.domain.order.entity.Order;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDto {
    private List<Long> menuIds;
    private Long storeId;

    public OrderRequestDto(Long storeId, List<Long> menuIds) {
        this.storeId = storeId;
        this.menuIds = menuIds;
    }

    Order order = new Order(

    );
}
