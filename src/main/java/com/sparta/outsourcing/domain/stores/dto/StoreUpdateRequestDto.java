package com.sparta.outsourcing.domain.stores.dto;

import lombok.Getter;

@Getter
public class StoreUpdateRequestDto {
    private String name;
    private int type;
    private String category;
    private String address;
    private String storePictureUrl;
    private String phone;
    private String contents;
    private int minDeliveryPrice;
    private int deliveryTip;
    private String operationHours;
    private String closedDays;

    public StoreUpdateRequestDto(String name, int type, String category, String address,
                                 String storePictureUrl, String phone, String contents,
                                 int minDeliveryPrice, int deliveryTip, String operationHours,
                                 String closedDays) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.address = address;
        this.storePictureUrl = storePictureUrl;
        this.phone = phone;
        this.contents = contents;
        this.minDeliveryPrice = minDeliveryPrice;
        this.deliveryTip = deliveryTip;
        this.operationHours = operationHours;
        this.closedDays = closedDays;
    }
}
