package com.sparta.outsourcing.domain.order.service;

import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.menu.repository.MenuRepository;
import com.sparta.outsourcing.domain.order.dto.OrderRequestDto;
import com.sparta.outsourcing.domain.order.entity.Order;
import com.sparta.outsourcing.domain.order.enums.OrderStatusEnum;
import com.sparta.outsourcing.domain.order.repository.OrderRepository;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.stores.repository.StoresRepository;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.User;
import com.sparta.outsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.outsourcing.domain.user.entity.Role.ROLE_USER;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoresRepository storeRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;


    @Transactional
    public Order createOrder(OrderRequestDto requestDto, CustomUserDetails userDetails) {
        // 사용자 역할 확인
        if (!userDetails.getRole().equals(ROLE_USER)) {
            throw new IllegalArgumentException("사용자 계정으로만 주문이 가능합니다.");
        }

        // 가게 찾기
        Long storeId = requestDto.getStoreId();
        Stores store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NullPointerException("해당 가게를 찾을 수 없습니다."));

        int totalPrice = 0;
        List<Menu> orderedMenus = new ArrayList<>();

        // 메뉴 ID 리스트 처리
        for (Long menuId : requestDto.getMenuIds()) {
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new NullPointerException("해당 메뉴를 찾을 수 없습니다."));
            orderedMenus.add(menu);  // 메뉴 추가
            totalPrice += menu.getPrice();  // 총 가격 합산
            System.out.println("메뉴 추가: " + menu.getName());
        }

        // 최소 주문 금액 체크
        if (totalPrice < store.getMinDeliveryPrice()) {
            throw new IllegalArgumentException("최소 주문 금액을 넘지 않습니다.");
        }

        // 가게 오픈/마감 시간 체크
        if (!store.isOpen()) {
            throw new NullPointerException("영업 중인 가게가 아닙니다.");
        }

        // 사용자 조회
        User user = userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new NullPointerException("사용자를 찾을 수 없습니다."));

        // 주문 생성 및 저장 (store, totalPrice, userDetails, 여러 메뉴를 포함)
        Order order = new Order(store, orderedMenus, user, totalPrice);
        return orderRepository.save(order);
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatusEnum status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NullPointerException("해당 주문을 찾을 수 없습니다."));

        order.updateStatus(status);
    }
}
