package com.sparta.outsourcing.order.service;

import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.menu.repository.MenuRepository;
import com.sparta.outsourcing.order.entity.Order;
import com.sparta.outsourcing.order.repository.OrderRepository;
import com.sparta.outsourcing.stores.entity.Stores;
import com.sparta.outsourcing.stores.repository.StoresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;
    private StoresRepository storeRepository;
    private MenuRepository menuRepository;

    @Transactional
    public Order createOrder(Long storeId, String menuName, int totalPrice) {
        Stores store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NullPointerException("해당 가게를 찾을 수 없습니다."));
        Menu menu = menuRepository.findByName(menuName)
                .orElseThrow(() -> new NullPointerException("해당 매뉴를 찾을 수 없습니다."));

        // 최소 주문 금액 체크
        if (totalPrice < store.getMinDeliveryPrice()) {
            throw new IllegalArgumentException("최소 주문 금액을 넘지 않습니다.");
        }

        // 가게 오픈/마감 시간 체크
        if (store.isOpen()) {
            throw new NullPointerException("영업 중인 가게가 아닙니다.");
        }

        Order order = new Order(store, menu, totalPrice);
        return orderRepository.save(order);
    }
}
