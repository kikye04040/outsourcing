package com.sparta.outsourcing.domain.menu.dto.response;

import lombok.Getter;

@Getter
public class MenuResponseDto {

    private String menuPictureUrl;

    private String name;

    private String description;

    private Integer price;


    public MenuResponseDto(String menuPictureUrl, String name, String description, Integer price) {
        this.menuPictureUrl = menuPictureUrl;
        this.name = name;
        this.description = description;
        this.price = price;
    }

}
