package com.sparta.outsourcing.domain.menu.dto.request;

import lombok.Getter;

@Getter
public class MenuUpdateRequest {

    private String name;
    private String description;
    private Integer price;

}
