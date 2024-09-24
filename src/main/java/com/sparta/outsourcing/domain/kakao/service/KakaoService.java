package com.sparta.outsourcing.domain.kakao.service;

import com.sparta.outsourcing.domain.kakao.dto.KakaoTokenResponseDto;
import com.sparta.outsourcing.domain.kakao.dto.KakaoUserDetails;
import com.sparta.outsourcing.domain.kakao.dto.KakaoUserInfoResponseDto;
import com.sparta.outsourcing.domain.kakao.entity.KakaoUser;
import com.sparta.outsourcing.domain.kakao.repository.KakaoUserRepository;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.domain.user.entity.TokenType;
import com.sparta.outsourcing.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final String clientId;
    private final String KAUTH_TOKEN_URL_HOST;
    private final String KAUTH_USER_URL_HOST;

    private final KakaoUserRepository kakaoUserRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public KakaoService(@Value("${kakao.client_id}") String clientId, KakaoUserRepository kakaoUserRepository, JwtUtil jwtUtil) {
        this.clientId = clientId;
        KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
        KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
        this.kakaoUserRepository = kakaoUserRepository;
        this.jwtUtil = jwtUtil;
    }

    public String getAccessTokenFromKakao(String code) {

        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(status -> status.is5xxServerError(), clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();

        log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
        log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
        log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());

        return kakaoTokenResponseDto.getAccessToken();
    }

    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {

        KakaoUserInfoResponseDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(status -> status.is5xxServerError(), clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        log.info("[ Kakao Service ] Auth ID ---> {} ", userInfo.getId());
        log.info("[ Kakao Service ] NickName ---> {} ", userInfo.getKakaoAccount().getProfile().getNickName());
        log.info("[ Kakao Service ] ProfileImageUrl ---> {} ", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());

        return userInfo;
    }

    public String registerOrLoginKakaoUser(KakaoUserInfoResponseDto userInfo) {
        String socialId = String.valueOf(userInfo.getId());
        String email = userInfo.getKakaoAccount().getEmail();
        String nickname = userInfo.getKakaoAccount().getProfile().getNickName();

        // 사용자 정보를 기반으로 사용자 찾기
        KakaoUser kakaoUser = kakaoUserRepository.findBySocialIdAndSocialType(socialId, "kakao")
                .orElseGet(() -> {
                    // 새로운 사용자 생성
                    KakaoUser newKakaoUser = new KakaoUser();
                    newKakaoUser.setSocialId(socialId);
                    newKakaoUser.setEmail(email);
                    newKakaoUser.setSocialType("kakao");
                    newKakaoUser.setNickname(nickname);
                    newKakaoUser.setRole(Role.ROLE_USER);
                    return kakaoUserRepository.save(newKakaoUser);
                });

        KakaoUserDetails kakaoUserDetails = new KakaoUserDetails(kakaoUser);
        return jwtUtil.createKakaoToken(TokenType.ACCESS, kakaoUserDetails); // 반환 타입을 String으로 변경
    }
}
