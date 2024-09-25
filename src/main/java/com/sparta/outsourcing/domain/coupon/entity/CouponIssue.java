package com.sparta.outsourcing.domain.coupon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class CouponIssue {

    @Id @GeneratedValue
    private Long id;

    private Long couponId;

    private String email;

    private LocalDateTime issuedDate;

    private LocalDateTime usedDate;
}
