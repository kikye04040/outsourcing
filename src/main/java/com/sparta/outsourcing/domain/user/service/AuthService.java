package com.sparta.outsourcing.domain.user.service;

import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.dto.request.JoinRequest;
import com.sparta.outsourcing.domain.user.dto.request.LoginRequest;
import com.sparta.outsourcing.domain.user.dto.response.JoinResponse;
import com.sparta.outsourcing.domain.user.dto.response.LoginResponse;
import com.sparta.outsourcing.domain.user.entity.Grade;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.domain.user.entity.Status;
import com.sparta.outsourcing.domain.user.entity.User;
import com.sparta.outsourcing.domain.user.repository.UserRepository;
import com.sparta.outsourcing.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //TODO: 환경 변수에서 읽어오게 할 것
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public JoinResponse join(JoinRequest joinRequest) {
        String email = joinRequest.getEmail();
        String password = passwordEncoder.encode(joinRequest.getPassword());
        String name = joinRequest.getName();
        String phone = joinRequest.getPhone();
        String currentAddress = joinRequest.getCurrentAddress();
        Role role = Role.ROLE_USER;


        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("중복된 사용자가 존재합니다");
        }

        if (isValidAdminToken(joinRequest.getAdminToken())) {
            role = Role.ROLE_ADMIN;
        }

        if (joinRequest.getIsOwner()!= null && joinRequest.getIsOwner()) {
            role = Role.ROLE_OWNER;
        }

        User user = User.builder()
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
                .currentAddress(currentAddress)
                .role(role)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .status(Status.NORMAL)
                .grade(Grade.THANKFUL)
                .build();

        userRepository.save(user);

        String token = jwtUtil.createToken(
                CustomUserDetails.builder()
                        .email(email)
                        .build(), role.name());

        return JoinResponse.builder().token(token).build();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(RuntimeException::new);
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(CustomUserDetails.builder()
                .email(user.getEmail())
                .build(), user.getRole().name());
        return LoginResponse.builder().token(token).name(user.getName()).build();
    }

    public boolean isValidAdminToken(String adminToken) {
        return adminToken != null && adminToken.equals(ADMIN_TOKEN);
    }
}
