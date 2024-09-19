package com.sparta.outsourcing.stores.dto;

import lombok.Getter;

@Getter
public class StoreCreatedRequestDto {
    private String name;
    private int type;
    private String category;
    private String address;
    private String phone;

    private String contents;
    private String storePictureUrl;
    private int deliveryTip;
    private String operationHours;
    private String closedDays;

    public StoreCreatedRequestDto(String name, int type, String category, String address, String phone, String contents, String storePictureUrl, int deliveryTip,
                                  String operationHours, String closedDays) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.address = address;
        this.phone = phone;
        this.contents = contents;
        this.storePictureUrl = storePictureUrl;
        this.deliveryTip = deliveryTip;
        this.operationHours = operationHours;
        this.closedDays = closedDays;
    }
}
