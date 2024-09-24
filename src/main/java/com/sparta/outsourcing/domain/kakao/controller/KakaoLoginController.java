package com.sparta.outsourcing.domain.kakao.controller;

import com.sparta.outsourcing.domain.kakao.dto.KakaoLoginResponse;
import com.sparta.outsourcing.domain.kakao.dto.KakaoUserInfoResponseDto;
import com.sparta.outsourcing.domain.kakao.service.KakaoService;
import com.sparta.outsourcing.domain.user.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {

    private final KakaoService kakaoService;

    @GetMapping("/callback")
    public ResponseEntity<KakaoLoginResponse> kakaoCallbackGet(@RequestParam String code) {
        return kakaoCallback(code); // POST 방식 처리 메서드 호출
    }

    @PostMapping("/callback") // POST 방식으로 변경
    public ResponseEntity<KakaoLoginResponse> kakaoCallback(@RequestParam String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);

        String jwtToken = kakaoService.registerOrLoginKakaoUser(userInfo);

        return ResponseEntity.ok(new KakaoLoginResponse(jwtToken, true)); // hasEssentialInfo는 실제 로직에 맞게 수정 필요
    }
}
