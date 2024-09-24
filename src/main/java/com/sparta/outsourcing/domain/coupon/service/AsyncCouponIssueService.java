package com.sparta.outsourcing.domain.coupon.service;

import com.sparta.outsourcing.domain.coupon.entity.CouponRedisEntity;
import com.sparta.outsourcing.domain.coupon.repository.CouponRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AsyncCouponIssueService {

    private final CouponRedisRepository redisRepository;
    private final CouponCacheService couponCacheService;

    public void issue(Long couponId, String email) {
        CouponRedisEntity coupon = couponCacheService.getCouponCache(couponId);
        coupon.checkIssuableCoupon();
        issueRequest(couponId, email, coupon.totalQuantity());
    }

    public void issueRequest(long couponId, String email, Integer totalIssueQuantity) {
        if (totalIssueQuantity == null) {
            redisRepository.issueRequest(couponId, email, Integer.MAX_VALUE);
        }
        redisRepository.issueRequest(couponId, email, totalIssueQuantity);
    }
}
