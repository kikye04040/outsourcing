package com.sparta.outsourcing.domain.order.aspect;

import com.sparta.outsourcing.domain.order.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class OrderLogAspect {

    // 주문이 생성될 때 로그 기록
    @AfterReturning(pointcut = "execution(* com.sparta.outsourcing.domain.order.service.OrderService.createOrder(..))", returning = "result")
    public void logCreateOrder(Object result) {
        if (result instanceof Order) {
            Order order = (Order) result;
            log.info("Order Created: [OrderId: {}, StoreId: {}, Menu: {}, TotalPrice: {}]",
                    order.getId(), order.getStore().getStoreId(), order.getMenu().getName(), order.getTotalPrice());
        }
    }

    // 주문 상태가 업데이트될 때 로그 기록
    @AfterReturning(pointcut = "execution(* com.sparta.outsourcing.domain.order.service.OrderService.updateOrderStatus(..))", returning = "result")
    public void logUpdateOrderStatus(Object result) {
        if (result instanceof Order) {
            Order order = (Order) result;
            log.info("Order Status Updated: [OrderId: {}, StoreId: {}, New Status: {}]",
                    order.getId(), order.getStore().getStoreId(), order.getStatus());
        }
    }
}
