package com.sparta.outsourcing.domain.coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CouponIssueRequestService {

    private final AsyncCouponIssueService asyncCouponIssueService;

    public void asyncIssueRequest(Long couponId, String email) {
        asyncCouponIssueService.issue(couponId, email);
    }
}
