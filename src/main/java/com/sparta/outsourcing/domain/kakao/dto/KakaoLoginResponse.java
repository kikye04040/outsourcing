package com.sparta.outsourcing.domain.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 모든 필드를 포함하는 생성자를 자동으로 생성
public class KakaoLoginResponse {
    private final String token;
    private final boolean hasEssentialInfo; // 또는 다른 타입으로 변경 가능
}
