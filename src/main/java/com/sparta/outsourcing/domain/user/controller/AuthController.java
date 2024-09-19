package com.sparta.outsourcing.domain.user.controller;

import com.sparta.outsourcing.domain.user.dto.request.JoinRequest;
import com.sparta.outsourcing.domain.user.dto.request.LoginRequest;
import com.sparta.outsourcing.domain.user.dto.response.JoinResponse;
import com.sparta.outsourcing.domain.user.dto.response.LoginResponse;
import com.sparta.outsourcing.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<JoinResponse> joinUser(@RequestBody JoinRequest joinRequest) {
        JoinResponse joinResponse = userService.join(joinRequest);
        return ResponseEntity.ok(joinResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
