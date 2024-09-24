package com.sparta.outsourcing.domain.menu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuUpdateRequestDto {

    private String menuPictureUrl;
    private String name;
    private String description;
    private Integer price;

}
