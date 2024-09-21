package com.sparta.outsourcing.domain.menu.dto.request;

import com.sparta.outsourcing.domain.stores.entity.Stores;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MenuCreateRequestDto {

    private String name;
    private String description;
    private Integer price;

}
