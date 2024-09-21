package com.sparta.outsourcing.domain.menu.dto.request;

import com.sparta.outsourcing.domain.stores.entity.Stores;
import lombok.Getter;

@Getter
public class MenuCreateRequestDto {

    private Long storeId;

    private String name;
    private String description;
    private Integer price;

}
