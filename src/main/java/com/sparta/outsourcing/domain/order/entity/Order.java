package com.sparta.outsourcing.domain.order.entity;

import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.order.enums.OrderStatusEnum;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Stores store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    private LocalDateTime createdAt;

    public Order(Stores store, Menu menu, Integer totalPrice) {
        this.store = store;
        this.menu = menu;
        this.totalPrice = totalPrice;
        this.status = OrderStatusEnum.NEW;
        this.createdAt = LocalDateTime.now();
    }

    public void updateStatus(OrderStatusEnum status) {
        this.status = status;
    }
}