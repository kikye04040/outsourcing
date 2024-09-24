package com.sparta.outsourcing.domain.coupon.service;

import com.sparta.outsourcing.domain.coupon.entity.Coupon;
import com.sparta.outsourcing.domain.coupon.entity.CouponIssue;
import com.sparta.outsourcing.domain.coupon.exception.CouponIssueException;
import com.sparta.outsourcing.domain.coupon.repository.CouponIssueJpaRepository;
import com.sparta.outsourcing.domain.coupon.repository.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CouponIssueService {

    private final CouponJpaRepository couponJpaRepository;
    private final CouponIssueJpaRepository couponIssueJpaRepository;

    @Transactional
    public void issue(Long couponId, String email) {
        Coupon coupon = findCouponWithLock(couponId);
        coupon.issue();
        saveCouponIssue(couponId, email);
    }

    public Coupon findCoupon(Long couponId) {
        return couponJpaRepository.findById(couponId).orElseThrow(() -> {
            throw new CouponIssueException("해당 쿠폰이 존재하지 않습니다.");
        });
    }

    public Coupon findCouponWithLock(long couponId) {
        return couponJpaRepository.findCouponWithLock(couponId).orElseThrow(() -> {
            throw new CouponIssueException("해당 쿠폰이 존재하지 않습니다.");
        });
    }

    public CouponIssue saveCouponIssue(long couponId, String email) {
        checkAlreadyIssuance(couponId, email);
        CouponIssue couponIssue = CouponIssue.builder()
                .couponId(couponId)
                .email(email)
                .issuedDate(LocalDateTime.now())
                .build();
        return couponIssueJpaRepository.save(couponIssue);
    }

    private void checkAlreadyIssuance(long couponId, String email) {
        Optional<CouponIssue> optionalCouponIssue = couponIssueJpaRepository.findCouponIssue(couponId, email);
        if (optionalCouponIssue.isPresent()) {
            throw new CouponIssueException("이미 발급된 쿠폰입니다.");
        }
    }
}
