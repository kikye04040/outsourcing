package com.sparta.outsourcing.stores.entity;

import com.sparta.outsourcing.stores.enums.StoreStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Stores {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

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

    private DecimalFormat rating;

    private int dibsCount;

    private int reviewCount;

    private String operationHours;

    private String closedDays;

    @Enumerated(EnumType.STRING)
    private StoreStatus storeStatus;


//    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
//    private List<Reviews> reviews = new ArrayList<>();

//    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
//    private List<Menu> Menu = new ArrayList<>();

//    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
//    private List<Orders> Orders = new ArrayList<>();

//    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
//    private List<Carts> Carts = new ArrayList<>();

    public Stores(String name, int type, String category, String address, String phone, String contents, String storePictureUrl, int deliveryTip,
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
        this.storeStatus = StoreStatus.Running;
    }


    public void update(String name, String address, String storePictureUrl, String phone, String contents, int minDeliveryPrice, int deliveryTip, String operationHours, String closedDays) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.contents = contents;
        this.storePictureUrl = storePictureUrl;
        this.deliveryTip = deliveryTip;
        this.operationHours = operationHours;
        this.closedDays = closedDays;
        this.minDeliveryPrice = minDeliveryPrice;
    }

    public void setStoreStatus(StoreStatus storeStatus){
        this.storeStatus = storeStatus;
    }
}
