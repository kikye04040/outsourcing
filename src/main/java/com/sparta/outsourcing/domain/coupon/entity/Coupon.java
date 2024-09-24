package com.sparta.outsourcing.domain.coupon.entity;

import com.sparta.outsourcing.domain.coupon.exception.CouponIssueException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(value = EnumType.STRING)
    private CouponType couponType;

    private Integer totalQuantity;

    private int issuedQuantity;

    private int discountAmount;

    private int minAvailableAmount;

    private LocalDateTime issueStartDate;

    private LocalDateTime issueEndDate;

    public boolean availableIssueQuantity() {
        if (totalQuantity == null) {
            return true;
        }
        return totalQuantity > issuedQuantity;
    }

    public boolean availableIssueDate() {
        LocalDateTime now = LocalDateTime.now();
        return issueStartDate.isBefore(now) && issueEndDate.isAfter(now);
    }

    public void issue() {
        if (!availableIssueQuantity()) {
            throw new CouponIssueException("발급 가능한 수량을 초과했습니다.");
        }
        if (!availableIssueDate()) {
            throw new CouponIssueException("발급 가능한 일자가 아닙니다.");
        }
        issuedQuantity++;
    }
}
