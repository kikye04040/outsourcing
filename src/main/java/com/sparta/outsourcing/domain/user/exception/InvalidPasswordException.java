package com.sparta.outsourcing.domain.user.exception;

import com.sparta.outsourcing.exception.BadRequestException;

public class InvalidPasswordException extends BadRequestException {
    private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public InvalidPasswordException() {super(MESSAGE);}

    public InvalidPasswordException(String message) {super(message);}
}
