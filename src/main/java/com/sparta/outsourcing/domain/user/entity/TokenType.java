package com.sparta.outsourcing.domain.user.entity;

public enum TokenType {
    ACCESS(600000L), // 10분
    REFRESH(86400000L); // 1일

    private final long expireMs;

    TokenType(long expireMs) {
        this.expireMs = expireMs;
    }

    public long getExpireMs() {
        return expireMs;
    }
}
