package com.sparta.outsourcing.domain.order.entity;

import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.order.enums.OrderStatusEnum;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id", nullable = false)
    private Stores store;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_menus",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    private List<Menu> menus = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    private LocalDateTime createdAt;

    // 여러 메뉴를 받는 생성자
    public Order(Stores store, List<Menu> menus, User user, Integer totalPrice) {
        this.store = store;
        this.menus = menus;
        this.user = user;
        this.totalPrice = totalPrice;
        this.status = OrderStatusEnum.NEW;
        this.createdAt = LocalDateTime.now();
    }

    public void updateStatus(OrderStatusEnum status) {
        this.status = status;
    }
}
