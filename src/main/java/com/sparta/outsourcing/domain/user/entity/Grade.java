package com.sparta.outsourcing.domain.user.entity;

import lombok.Getter;

@Getter
public enum Grade {
    THANKFUL("고마운분"),
    VALUABLE("귀한분"),
    MORE_VALUABLE("더 귀한분"),
    SOULMATE("천생연분");

    private final String name;

    private Grade(String name) {this.name = name;}

}
