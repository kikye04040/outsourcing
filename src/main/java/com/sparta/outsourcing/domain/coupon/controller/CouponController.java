package com.sparta.outsourcing.domain.coupon.controller;

import com.sparta.outsourcing.domain.coupon.dto.request.CouponCreateRequest;
import com.sparta.outsourcing.domain.coupon.dto.response.CouponCreateResponse;
import com.sparta.outsourcing.domain.coupon.dto.response.CouponIssueResponseDto;
import com.sparta.outsourcing.domain.coupon.service.CouponIssueRequestService;
import com.sparta.outsourcing.domain.coupon.service.CouponService;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class CouponController {

    private final CouponIssueRequestService couponIssueRequestService;
    private final CouponService couponService;

    @PostMapping("/coupons/{couponId}")
    public ResponseEntity<CouponIssueResponseDto> issueAsync(@PathVariable Long couponId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        couponIssueRequestService.asyncIssueRequest(couponId, userDetails.getEmail());
        return ResponseEntity.ok(new CouponIssueResponseDto(true, "쿠폰 발급에 성공했습니다!"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/coupons")
    public ResponseEntity<CouponCreateResponse> createCoupon(@RequestBody CouponCreateRequest couponCreateRequest) {
        String couponTitle = couponService.saveCoupon(couponCreateRequest);
        return ResponseEntity.ok(new CouponCreateResponse(couponTitle, "쿠폰 생성 성공"));
    }
}
