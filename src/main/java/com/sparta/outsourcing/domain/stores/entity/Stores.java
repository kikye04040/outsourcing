package com.sparta.outsourcing.domain.stores.entity;

import com.sparta.outsourcing.domain.stores.enums.StoreStatus;
import com.sparta.outsourcing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor
public class Stores {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Enumerated(EnumType.STRING)
    private StoreStatus storeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
//    private List<Review> reviews = new ArrayList<>();

//    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
//    private List<Menu> Menu = new ArrayList<>();

//    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
//    private List<Order> Orders = new ArrayList<>();

//    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
//    private List<Carts> Carts = new ArrayList<>();

    public Stores(String name, int type, String category, String address, String phone, String contents, String storePictureUrl, int deliveryTip,
                  String operationHours, String closedDays, User user, int minDeliveryPrice) {
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
        this.user = user;
        this.minDeliveryPrice = minDeliveryPrice;
    }


    public void update(String name, int type, String category, String address,
                       String storePictureUrl, String phone, String contents,
                       int deliveryTip, String operationHours, String closedDays, int minDeliveryPrice) {
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
        this.minDeliveryPrice = minDeliveryPrice;
    }

    public void setStoreStatus(StoreStatus storeStatus){
        this.storeStatus = storeStatus;
    }

    // 가게가 현재 영업 중인지 체크하는 메서드
    public boolean isOpen() {
        // 현재 시간이 가게의 오픈 시간과 마감 시간 사이에 있는지 확인
        String[] hours = operationHours.split("~");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime openTime = LocalTime.parse(hours[0], formatter);
        LocalTime closeTime = LocalTime.parse(hours[1], formatter);
        LocalTime now = LocalTime.now();

        // 오늘이 영업일인지 확인 (closedDays가 있으면 영업하지 않음)
        String today = LocalDate.now().getDayOfWeek().toString();  // 예: "SUNDAY"
        if (closedDays != null && closedDays.toUpperCase().contains(today)) {
            return false;  // 오늘은 영업하지 않음
        }

        return now.isAfter(openTime) && now.isBefore(closeTime);
    }
}
