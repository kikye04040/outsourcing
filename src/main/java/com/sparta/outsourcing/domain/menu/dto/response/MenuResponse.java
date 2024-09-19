package com.sparta.outsourcing.domain.menu.dto.response;

import com.sparta.outsourcing.domain.menu.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponse {

    private String name;

    private String description;

    private Integer price;


    public MenuResponse(String name, String description, Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

}
