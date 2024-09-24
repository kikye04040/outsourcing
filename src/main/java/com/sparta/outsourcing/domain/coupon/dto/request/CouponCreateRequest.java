package com.sparta.outsourcing.domain.coupon.dto.request;

public record CouponCreateRequest(
        String title,
        Integer totalQuantity,
        Integer discountAmount,
        Integer minAvailableAmount
) {
}
