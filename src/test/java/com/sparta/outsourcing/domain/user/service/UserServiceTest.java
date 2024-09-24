package com.sparta.outsourcing.domain.user.service;

import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.dto.request.WithdrawRequest;
import com.sparta.outsourcing.domain.user.dto.response.UserListResponse;
import com.sparta.outsourcing.domain.user.dto.response.UserResponse;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.domain.user.entity.Status;
import com.sparta.outsourcing.domain.user.entity.User;
import com.sparta.outsourcing.domain.user.exception.InvalidPasswordException;
import com.sparta.outsourcing.domain.user.exception.UserNotFoundException;
import com.sparta.outsourcing.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private CustomUserDetails customUserDetails;
    private WithdrawRequest withdrawRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("testuser@example.com")
                .password("encodedPassword")
                .name("Test User")
                .createdDate(LocalDateTime.now())
                .status(Status.NORMAL)
                .role(Role.ROLE_USER)
                .build();

        customUserDetails = CustomUserDetails.builder()
                .email("testuset@example.com").build();

        withdrawRequest = WithdrawRequest.builder()
                .password("rowPassword").build();
    }

    @DisplayName("이메일로 사용자 조회에 성공한다.")
    @Test
    void givenEmailWhenFindByEmailThenReturnsUserResponse() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // When
        UserResponse result = userService.findByEmail("testuser@example.com");

        // Then
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @DisplayName("이메일로 없는 사용자를 조회시 예외를 반환한다.")
    @Test
    void givenEmailWhenFindByEmailThenThrowsException() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> userService.findByEmail("testuser@example.com"))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @DisplayName("Id 로 사용자 조회에 성공한다.")
    @Test
    void givenIdWhenFindByIdThenReturnsUserResponse() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // When
        UserResponse result = userService.findById(1L);

        // Then
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @DisplayName("Id로 없는 사용자를 조회시 예외를 반환한다.")
    @Test
    void findById_userNotFound() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> userService.findById(1L))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository, times(1)).findById(anyLong());
    }

    @DisplayName("모든 사용자 조회에 성공한다.")
    @Test
    void findAll_success() {
        // Given
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);

        // When
        UserListResponse result = userService.findAll();

        // Then
        assertThat(result.getUserResponseList()).hasSize(1);
        verify(userRepository, times(1)).findAll();
    }

    @DisplayName("회원탈퇴에 성공한다")
    @Test
    void withDraw_success() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // When
        userService.withDraw(withdrawRequest, customUserDetails);

        // Then
        verify(userRepository, times(1)).delete(user);  // 사용자가 성공적으로 삭제되는지 확인
    }

    @DisplayName("회원탈퇴시 비밀번호가 일치하지 않으면 예외를 반환한다.")
    @Test
    void withDraw_invalidPassword() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);  // 비밀번호 불일치

        // When & Then
        assertThrows(InvalidPasswordException.class, () -> userService.withDraw(withdrawRequest, customUserDetails));

        verify(userRepository, never()).delete(any());  // 비밀번호가 틀리면 삭제되지 않음
    }

}