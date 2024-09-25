package com.sparta.outsourcing.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserListResponse {
    List<UserResponse> userResponseList;
}
