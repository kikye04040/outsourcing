package com.sparta.outsourcing.domain.user.exception;

import com.sparta.outsourcing.exception.BadRequestException;

public class TokenNotFoundException extends BadRequestException {
    private static final String MESSAGE = "토큰이 존재하지 않습니다.";

    public TokenNotFoundException() {super(MESSAGE);}

    public TokenNotFoundException(String message) {super(message);}
}
