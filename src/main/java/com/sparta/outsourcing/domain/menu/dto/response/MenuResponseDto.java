package com.sparta.outsourcing.domain.menu.dto.response;

import lombok.Getter;

@Getter
public class MenuResponseDto {

    private String name;

    private String description;

    private Integer price;


    public MenuResponseDto(String name, String description, Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

}
