package com.sparta.outsourcing.jwt;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    TOKEN_EXPIRED(401, "토큰의 유효기간이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(400, "유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND(400, "토큰이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
