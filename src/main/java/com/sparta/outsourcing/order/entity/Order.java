package com.sparta.outsourcing.order.entity;

import com.sparta.outsourcing.order.orderstatusenum.OrderStatus;
import com.sparta.outsourcing.stores.entity.Stores;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.Store;

import java.awt.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Stores store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private int totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime orderTime;

    public Order(Stores store, Menu menu, int totalPrice) {
        this.store = store;
        this.menu = menu;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderTime = orderTime;
    }
}