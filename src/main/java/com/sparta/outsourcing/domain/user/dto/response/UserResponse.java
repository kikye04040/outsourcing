package com.sparta.outsourcing.domain.user.dto.response;

import com.sparta.outsourcing.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder(access = AccessLevel.PROTECTED)
@Getter
public class UserResponse {
    private String email;
    private String createdDate;
    private String name;
    private String currentAddress;
    private String phone;
    private String role;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .createdDate(user.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")))
                .name(user.getName())
                .currentAddress(user.getCurrentAddress())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .build();
    }

}
