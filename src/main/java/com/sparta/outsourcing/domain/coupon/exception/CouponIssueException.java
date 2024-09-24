package com.sparta.outsourcing.domain.coupon.exception;

import com.sparta.outsourcing.exception.BadRequestException;

public class CouponIssueException extends BadRequestException {
    private static final String MESSAGE = "잘못된 쿠폰 발급요청입니다.";

    public CouponIssueException() {super(MESSAGE);}

    public CouponIssueException(String message) {super(message);}
}
