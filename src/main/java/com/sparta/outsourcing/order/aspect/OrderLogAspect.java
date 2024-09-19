package com.sparta.outsourcing.order.aspect;

import com.sparta.outsourcing.order.dto.OrderResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class OrderLogAspect {

    @Pointcut("execution(* com.sparta.outsourcing.order.service.OrderService.createOrder(..))")
    public void orderMethods() {}

    @AfterReturning(pointcut = "orderMethods()", returning = "result")
    public void logOrderChanges(JoinPoint joinPoint, Object result) {
        LocalDateTime requestTime = LocalDateTime.now();
        if (result instanceof OrderResponseDto) {
            OrderResponseDto responseDto = (OrderResponseDto) result;
            log.info("Order created - Time: {}, Store ID: {}, Order ID: {}", requestTime, responseDto.getStoreId(), responseDto.getOrderId());
        }
    }
}
