package com.sparta.outsourcing.stores.dto;

import lombok.Getter;

@Getter
public class StoresSimpleResponseDto {
    private String name;
    private String storePictureUrl;
    private int dibsCount;
    private int reviewCount;
    private int deliveryTip;
    private int minDeliveryTime;
    private int maxDeliveryTime;

    public StoresSimpleResponseDto(String name, String storePictureUrl, int dibsCount, int reviewCount, int deliveryTip, int minDeliveryTime, int maxDeliveryTime) {
        this.name = name;
        this.dibsCount = dibsCount;
        this.reviewCount = reviewCount;
        this.storePictureUrl = storePictureUrl;
        this.deliveryTip = deliveryTip;
        this.minDeliveryTime = minDeliveryTime;
        this.maxDeliveryTime = maxDeliveryTime;
    }
}
