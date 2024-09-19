package com.sparta.outsourcing.stores.dto;

import lombok.Getter;

@Getter
public class StoreDetailResponseDto {

    private String name;

    private int type;

    private String category;

    private String address;

    private String storePictureUrl;

    private String phone;

    private String contents;

    private int minDeliveryPrice;

    private int deliveryTip;

    private int minDeliveryTime;

    private int maxDeliveryTime;

    private int dibsCount;

    private int reviewCount;

    private String operationHours;

    private String closedDays;

    public StoreDetailResponseDto(String name, int type, String category, String address, String storePictureUrl, String phone, String contents, int minDeliveryPrice, int deliveryTip, int minDeliveryTime, int maxDeliveryTime, int dibsCount, int reviewCount, String operationHours, String closedDays) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.address = address;
        this.phone = phone;
        this.contents = contents;
        this.storePictureUrl = storePictureUrl;
        this.minDeliveryPrice = minDeliveryPrice;
        this.deliveryTip = deliveryTip;
        this.minDeliveryTime = minDeliveryTime;
        this.maxDeliveryTime = maxDeliveryTime;
        this.dibsCount = dibsCount;
        this.reviewCount = reviewCount;
        this.operationHours = operationHours;
        this.closedDays = closedDays;
    }
}
