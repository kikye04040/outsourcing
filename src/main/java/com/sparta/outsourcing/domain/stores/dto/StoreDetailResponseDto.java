package com.sparta.outsourcing.domain.stores.dto;

import com.sparta.outsourcing.domain.menu.dto.response.MenuResponseDto;
import com.sparta.outsourcing.domain.menu.entity.Menu;
import lombok.Getter;

import java.util.List;

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
    private String operationHours;

    private String closedDays;
    private List<MenuResponseDto> menuList;

    public StoreDetailResponseDto(String name, int type, String category, String address, String storePictureUrl, String phone, String contents, int minDeliveryPrice, int deliveryTip, String operationHours, String closedDays, List<MenuResponseDto> menuList) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.address = address;
        this.phone = phone;
        this.contents = contents;
        this.storePictureUrl = storePictureUrl;
        this.minDeliveryPrice = minDeliveryPrice;
        this.deliveryTip = deliveryTip;
        this.operationHours = operationHours;
        this.closedDays = closedDays;
        this.menuList = menuList;
    }
}
