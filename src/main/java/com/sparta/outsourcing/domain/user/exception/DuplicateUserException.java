package com.sparta.outsourcing.domain.user.exception;

import com.sparta.outsourcing.exception.BadRequestException;

public class DuplicateUserException extends BadRequestException {
    private static final String MESSAGE = "중복된 사용자가 존재합니다.";

    public DuplicateUserException() {super(MESSAGE);}

    public DuplicateUserException(String message) {super(message);}
}
