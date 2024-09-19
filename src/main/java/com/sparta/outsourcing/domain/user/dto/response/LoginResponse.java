package com.sparta.outsourcing.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponse {
    private String token;
    private String name;
}
