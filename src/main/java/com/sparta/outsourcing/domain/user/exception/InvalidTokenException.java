package com.sparta.outsourcing.domain.user.exception;

import com.sparta.outsourcing.exception.BadRequestException;

public class InvalidTokenException extends BadRequestException {
    private static final String MESSAGE = "올바르지 않은 토큰입니다.";

    public InvalidTokenException() {super(MESSAGE);}

    public InvalidTokenException(String message) {super(message);}
}
