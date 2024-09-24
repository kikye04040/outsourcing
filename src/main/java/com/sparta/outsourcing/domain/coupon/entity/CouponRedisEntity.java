package com.sparta.outsourcing.domain.coupon.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sparta.outsourcing.domain.coupon.exception.CouponIssueException;

import java.time.LocalDateTime;

public record CouponRedisEntity(
        Long id,
        CouponType couponType,
        Integer totalQuantity,

        boolean availableIssueQuantity,

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime dateIssueStart,

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime dateIssueEnd
) {
    public CouponRedisEntity(Coupon coupon) {
        this(
                coupon.getId(),
                coupon.getCouponType(),
                coupon.getTotalQuantity(),
                coupon.availableIssueQuantity(),
                coupon.getIssueStartDate(),
                coupon.getIssueEndDate()
        );
    }

    private boolean availableIssueDate() {
        LocalDateTime now = LocalDateTime.now();
        return dateIssueStart.isBefore(now) && dateIssueEnd.isAfter(now);
    }

    public void checkIssuableCoupon() {
        if (!availableIssueQuantity) {
            throw new CouponIssueException("모든 발급 수량이 소진되었습니다.");
        }
        if (!availableIssueDate()) {
            throw new CouponIssueException("발급 가능한 일자가 아닙니다.");
        }
    }
}
