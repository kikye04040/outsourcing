package com.sparta.outsourcing.domain.stores.dto;

import lombok.Getter;

@Getter
public class StoreResponseDto {
    private String message;
    private String name;
    private Integer status;

    public StoreResponseDto(String message, String name, Integer status) {
        this.message = message;
        this.name = name;
        this.status = status;
    }
}
