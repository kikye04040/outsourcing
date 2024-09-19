package com.sparta.outsourcing.domain.user.exception;

import com.sparta.outsourcing.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    private static final String MESSAGE = "회원정보를 찾을 수 없습니다.";

    public UserNotFoundException() {super(MESSAGE);}

    public UserNotFoundException(String message) {super(message);}
}
