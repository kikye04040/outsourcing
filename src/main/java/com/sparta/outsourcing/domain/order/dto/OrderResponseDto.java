package com.sparta.outsourcing.domain.order.dto;

import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.order.entity.Order;
import com.sparta.outsourcing.domain.order.enums.OrderStatusEnum;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponseDto {
    private Long orderId;
    private String storeName;
    private List<String> menuNames;
    private OrderStatusEnum status;
    private Integer totalPrice;
    private LogResponseDto log;

    public OrderResponseDto(Order order) {
        this.orderId = order.getId();
        this.storeName = order.getStore().getName();
        this.menuNames = order.getMenus().stream()
                .map(Menu::getName)
                .collect(Collectors.toList());
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.log = new LogResponseDto(order.getId(), order.getStore().getId());
    }
}
