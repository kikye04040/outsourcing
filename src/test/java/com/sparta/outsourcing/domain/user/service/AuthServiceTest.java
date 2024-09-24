package com.sparta.outsourcing.domain.user.service;

import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.dto.TokenDto;
import com.sparta.outsourcing.domain.user.dto.request.JoinRequest;
import com.sparta.outsourcing.domain.user.dto.response.TokenResponse;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.domain.user.entity.Status;
import com.sparta.outsourcing.domain.user.entity.User;
import com.sparta.outsourcing.domain.user.exception.DuplicateUserException;
import com.sparta.outsourcing.domain.user.exception.InvalidTokenException;
import com.sparta.outsourcing.domain.user.exception.WithdrawnUserException;
import com.sparta.outsourcing.domain.user.repository.UserRepository;
import com.sparta.outsourcing.jwt.JwtUtil;
import com.sparta.outsourcing.jwt.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService authService;

    @DisplayName("회원가입에 성공한다.")
    @Test
    void join_success() {
        // Given
        JoinRequest joinRequest = JoinRequest.builder()
                .email("test@example.com")
                .password("Password123!")
                .name("John Doe")
                .phone("010-1234-5678")
                .currentAddress("Seoul")
                .adminToken(null)
                .isOwner(false)
                .build();

        when(userRepository.findByEmailIncludingWithdrawn(joinRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(joinRequest.getPassword())).thenReturn("encryptedPassword");
        when(jwtUtil.createToken(eq("refresh"), any(CustomUserDetails.class), anyString(), anyLong()))
                .thenReturn("Bearer refreshToken"); // refreshToken 반환
        when(jwtUtil.createToken(eq("access"), any(CustomUserDetails.class), anyString(), anyLong()))
                .thenReturn("Bearer accessToken"); // accessToken 반환
        when(jwtUtil.substringToken(anyString())).thenReturn("refreshToken");

        // When
        TokenResponse tokenResponse = authService.join(joinRequest);

        // Then
        assertThat(tokenResponse.getAccessToken()).isEqualTo("Bearer accessToken");
        assertThat(tokenResponse.getRefreshToken()).isEqualTo("refreshToken");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("회원가입시 중복된 이메일일 경우 예외를 던진다.")
    @Test
    void join_duplicateEmail_throwsDuplicateUserException() {
        // Given
        JoinRequest joinRequest = JoinRequest.builder()
                .email("test@example.com")
                .password("password123")
                .name("John Doe")
                .phone("010-1234-5678")
                .currentAddress("Seoul")
                .build();

        User user = User.builder()
                .email("testuser@example.com")
                .password("encodedPassword")
                .name("Test User")
                .createdDate(LocalDateTime.now())
                .status(Status.NORMAL)
                .role(Role.ROLE_USER)
                .build();


        when(userRepository.findByEmailIncludingWithdrawn(joinRequest.getEmail())).thenReturn(Optional.of(user));

        // When & Then
        assertThrows(DuplicateUserException.class, () -> authService.join(joinRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("회원가입시 이미 탈퇴한 유저일 경우 예외를 던진다.")
    @Test
    void join_withdrawnUser_throwsWithdrawnUserException() {
        // Given
        JoinRequest joinRequest = JoinRequest.builder()
                .email("test@example.com")
                .password("password123")
                .name("John Doe")
                .phone("010-1234-5678")
                .currentAddress("Seoul")
                .build();

        User user = User.builder()
                .email("testuser@example.com")
                .password("encodedPassword")
                .name("Test User")
                .createdDate(LocalDateTime.now())
                .status(Status.WITHDRAWN)
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByEmailIncludingWithdrawn(joinRequest.getEmail())).thenReturn(Optional.of(user));

        // When & Then
        assertThrows(WithdrawnUserException.class, () -> authService.join(joinRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("토큰 재발급에 성공한다.")
    @Test
    void reissueAccessToken_success() {
        // Given
        String refreshToken = "validRefreshToken";
        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .email("test@example.com")
                .role(Role.ROLE_USER)
                .build();

        when(jwtUtil.getCategory(refreshToken)).thenReturn("refresh");
        when(jwtUtil.getCustomUserDetailsFromToken(refreshToken)).thenReturn(customUserDetails);
        when(refreshTokenService.getRefreshToken(customUserDetails.getEmail())).thenReturn("validRefreshToken");
        when(jwtUtil.createToken(anyString(), any(CustomUserDetails.class), anyString(), anyLong()))
                .thenReturn("Bearer newRefreshToken", "Bearer newAccessToken");
        when(jwtUtil.substringToken(anyString())).thenReturn("newRefreshToken");

        // When
        TokenDto tokenDto = authService.reissueAccessToken(refreshToken);

        // Then
        assertThat(tokenDto.getAccessToken()).isEqualTo("Bearer newAccessToken");
        assertThat(tokenDto.getRefreshToken()).isEqualTo("newRefreshToken");
        verify(refreshTokenService, times(1)).deleteRefreshToken(customUserDetails.getEmail());
        verify(refreshTokenService, times(1)).saveRefreshToken(anyString(), eq(customUserDetails.getEmail()), eq(24L), eq(TimeUnit.HOURS));
    }

    @DisplayName("토큰재발급시 잘못된 refresh 토큰일 경우 예외를 던진다.")
    @Test
    void reissueAccessToken_invalidRefreshToken_throwsInvalidTokenException() {
        // Given
        String refreshToken = "invalidRefreshToken";

        when(jwtUtil.getCategory(refreshToken)).thenThrow(new InvalidTokenException());

        // When & Then
        assertThatThrownBy(() -> authService.reissueAccessToken(refreshToken))
                .isInstanceOf(InvalidTokenException.class);

        verify(refreshTokenService, never()).deleteRefreshToken(anyString());
        verify(refreshTokenService, never()).saveRefreshToken(anyString(), anyString(), anyLong(), any());
    }

}