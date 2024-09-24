package com.sparta.outsourcing.domain.user.controller;

import com.sparta.outsourcing.domain.user.dto.TokenDto;
import com.sparta.outsourcing.domain.user.dto.request.JoinRequest;
import com.sparta.outsourcing.domain.user.dto.response.TokenResponse;
import com.sparta.outsourcing.domain.user.exception.TokenNotFoundException;
import com.sparta.outsourcing.domain.user.service.AuthService;
import com.sparta.outsourcing.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/join")
    public ResponseEntity<TokenResponse> joinUser(@Valid @RequestBody JoinRequest joinRequest) {
        TokenResponse tokenResponse = authService.join(joinRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(HttpServletRequest request, HttpServletResponse response) {
        TokenDto tokenDto = null;

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new TokenNotFoundException();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                tokenDto = authService.reissueAccessToken(cookie.getValue());
            }
        }

        if (tokenDto == null) {
            throw new TokenNotFoundException();
        }

        jwtUtil.addJwtToHeader(tokenDto.getAccessToken(), response);
        response.addCookie(createCooke("refresh", tokenDto.getRefreshToken()));

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();

        return ResponseEntity.ok(tokenResponse);
    }

    public Cookie createCooke(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        return cookie;
    }
}
