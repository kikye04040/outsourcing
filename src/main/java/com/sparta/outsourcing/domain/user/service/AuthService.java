package com.sparta.outsourcing.domain.user.service;

import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.dto.TokenDto;
import com.sparta.outsourcing.domain.user.dto.request.JoinRequest;
import com.sparta.outsourcing.domain.user.dto.response.TokenResponse;
import com.sparta.outsourcing.domain.user.entity.Grade;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.domain.user.entity.Status;
import com.sparta.outsourcing.domain.user.entity.User;
import com.sparta.outsourcing.domain.user.exception.DuplicateUserException;
import com.sparta.outsourcing.domain.user.exception.InvalidTokenException;
import com.sparta.outsourcing.domain.user.exception.WithdrawnUserException;
import com.sparta.outsourcing.domain.user.repository.UserRepository;
import com.sparta.outsourcing.jwt.JwtUtil;
import com.sparta.outsourcing.jwt.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    //TODO: 환경 변수에서 읽어오게 할 것
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public TokenResponse join(JoinRequest joinRequest) {
        String email = joinRequest.getEmail();
        String password = passwordEncoder.encode(joinRequest.getPassword());
        String name = joinRequest.getName();
        String phone = joinRequest.getPhone();
        String currentAddress = joinRequest.getCurrentAddress();
        Role role = Role.ROLE_USER;

        Optional<User> optionalUser = userRepository.findByEmailIncludingWithdrawn(email);
        if (optionalUser.isPresent()) {
            if (optionalUser.get().getStatus() == Status.WITHDRAWN) {
                throw new WithdrawnUserException("탈퇴한 사용자의 아이디는 재사용할 수 없습니다.");
            }
            throw new DuplicateUserException();
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

        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .email(email)
                .role(role)
                .build();

        String refreshTokenBearer = jwtUtil.createToken("refresh", customUserDetails, role.name(), 86400000L);
        String refreshToken = jwtUtil.substringToken(refreshTokenBearer);
        String token = jwtUtil.createToken("access", customUserDetails, role.name(), 600000L);

        return TokenResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenDto reissueAccessToken(String refreshToken) {

        if (refreshToken == null) {
            throw new InvalidTokenException();
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("만료된 토큰입니다.");
        }

        String category = jwtUtil.getCategory(refreshToken);

        if (!category.equals("refresh")) {
            throw new InvalidTokenException();
        }

        CustomUserDetails customUserDetails = jwtUtil.getCustomUserDetailsFromToken(refreshToken);
        String email = customUserDetails.getEmail();
        String role = customUserDetails.getRole().name();

        String storedRefreshToken = refreshTokenService.getRefreshToken(email);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new InvalidTokenException();
        }

        String newRefreshTokenBearer = jwtUtil.createToken("refresh", customUserDetails, role, 86400000L);
        String newAccessToken = jwtUtil.createToken("access", customUserDetails, role, 600000L);

        String newRefreshToken = jwtUtil.substringToken(newRefreshTokenBearer);

        refreshTokenService.deleteRefreshToken(email);
        refreshTokenService.saveRefreshToken(newRefreshToken, email, 24, TimeUnit.HOURS);

        return TokenDto.builder()
                .refreshToken(newRefreshToken)
                .accessToken(newAccessToken)
                .build();
    }

    public boolean isValidAdminToken(String adminToken) {
        return adminToken != null && adminToken.equals(ADMIN_TOKEN);
    }
}
