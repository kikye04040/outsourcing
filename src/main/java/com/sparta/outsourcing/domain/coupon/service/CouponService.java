package com.sparta.outsourcing.domain.coupon.service;

import com.sparta.outsourcing.domain.coupon.dto.request.CouponCreateRequest;
import com.sparta.outsourcing.domain.coupon.entity.Coupon;
import com.sparta.outsourcing.domain.coupon.entity.CouponType;
import com.sparta.outsourcing.domain.coupon.repository.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CouponService {

    private final CouponJpaRepository couponJpaRepository;

    @Transactional
    public String saveCoupon(CouponCreateRequest couponCreateRequest) {
        Coupon coupon = Coupon.builder()
                .title(couponCreateRequest.title())
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .totalQuantity(couponCreateRequest.totalQuantity())
                .discountAmount(couponCreateRequest.discountAmount())
                .minAvailableAmount(couponCreateRequest.minAvailableAmount())
                .issueStartDate(LocalDateTime.now())
                .issueEndDate(LocalDateTime.now().plusHours(1))
                .build();

        return couponJpaRepository.save(coupon).getTitle();
    }
}
