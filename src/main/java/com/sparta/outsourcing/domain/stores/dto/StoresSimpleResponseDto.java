package com.sparta.outsourcing.domain.stores.dto;

import lombok.Getter;

@Getter
public class StoresSimpleResponseDto {
    private String name;
    private String storePictureUrl;
    private int deliveryTip;

    public StoresSimpleResponseDto(String name, String storePictureUrl, int deliveryTip) {
        this.name = name;
        this.storePictureUrl = storePictureUrl;
        this.deliveryTip = deliveryTip;
    }
}
