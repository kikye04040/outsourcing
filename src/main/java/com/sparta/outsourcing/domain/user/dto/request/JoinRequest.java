package com.sparta.outsourcing.domain.user.dto.request;

import lombok.Getter;

@Getter
public class JoinRequest {
    private String email;
    private String password;
    private String phone;
    private String name;
    private String currentAddress;
    private String adminToken;
    private Boolean isOwner;
}
