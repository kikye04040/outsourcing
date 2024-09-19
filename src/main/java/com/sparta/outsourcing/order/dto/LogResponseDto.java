package com.sparta.outsourcing.order.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LogResponseDto {
    private String timestamp;
    private Long storeId;
    private Long orderId;

    public LogResponseDto(Long orderId, Long storeId) {
        this.timestamp = LocalDateTime.now().toString();
        this.storeId = storeId;
        this.orderId = orderId;
    }
}
