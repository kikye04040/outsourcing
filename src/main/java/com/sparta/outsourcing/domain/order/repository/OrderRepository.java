package com.sparta.outsourcing.domain.order.repository;

import com.sparta.outsourcing.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}