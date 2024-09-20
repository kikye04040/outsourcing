package com.sparta.outsourcing.domain.user.dto.response;

import com.sparta.outsourcing.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Builder(access = AccessLevel.PROTECTED)
@Getter
public class UserResponse {
    private String email;
    private String createdDate;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .createdDate(user.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")))
                .build();
    }
}
