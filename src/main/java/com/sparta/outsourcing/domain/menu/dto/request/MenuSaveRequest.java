package com.sparta.outsourcing.domain.menu.dto.request;

import lombok.Getter;

@Getter
public class MenuSaveRequest {

    private Long storeId;

    private String name;
    private String description;
    private Integer price;

}
