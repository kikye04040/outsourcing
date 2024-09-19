package com.sparta.outsourcing.domain.user.entity;

import lombok.Getter;

@Getter
public enum Status {
    NORMAL("일반"),
    SLEEPING("휴면"),
    WITHDRAWN("탈퇴"),
    BANNED("정지"),
    REVIEW_BANNED("리뷰금지");

    private final String description;

    private Status(String description) {
        this.description = description;
    }

}
