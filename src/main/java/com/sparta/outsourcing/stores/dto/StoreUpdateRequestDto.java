package com.sparta.outsourcing.stores.dto;

import lombok.Getter;

@Getter
public class StoreUpdateRequestDto {
    private String name;
    private String address;
    private String storePictureUrl;
    private String phone;
    private String contents;
    private int minDeliveryPrice;
    private int deliveryTip;
    private String operationHours;
    private String closedDays;
}
