package com.sparta.outsourcing.domain.user.controller;

import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.dto.request.WithdrawRequest;
import com.sparta.outsourcing.domain.user.dto.response.UserListResponse;
import com.sparta.outsourcing.domain.user.dto.response.UserResponse;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.domain.user.service.UserService;
import com.sparta.outsourcing.jwt.JwtUtil;
import com.sparta.outsourcing.jwt.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private RefreshTokenService refreshTokenService;


    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    void getMe_success() throws Exception {
        // Given
        CustomUserDetails userDetails = CustomUserDetails.builder()
                .email("test@example.com")
                .role(Role.ROLE_USER)
                .build();

        UserResponse userResponse = UserResponse.builder()
                .email("test@example.com")
                .name("John Doe")
                .build();

        when(userService.findByEmail(userDetails.getEmail())).thenReturn(userResponse);

        // When & Then
        mockMvc.perform(get("/users/me")
                        .with(user(userDetails)))  // Authentication principal 설정
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void getUser_asAdmin_success() throws Exception {
        // Given
        Long userId = 1L;
        UserResponse userResponse = UserResponse.builder()
                .email("testuser@example.com")
                .name("Test User")
                .build();

        when(userService.findById(userId)).thenReturn(userResponse);

        // When & Then
        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("testuser@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void getUsers_asAdmin_success() throws Exception {
        // Given
        UserListResponse userListResponse = UserListResponse.builder()
                .userResponseList(List.of(
                        UserResponse.builder().email("user1@example.com").name("User One").build(),
                        UserResponse.builder().email("user2@example.com").name("User Two").build()
                ))
                .build();

        when(userService.findAll()).thenReturn(userListResponse);

        // When & Then
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].email").value("user1@example.com"))
                .andExpect(jsonPath("$.users[0].name").value("User One"))
                .andExpect(jsonPath("$.users[1].email").value("user2@example.com"))
                .andExpect(jsonPath("$.users[1].name").value("User Two"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    void withdraw_success() throws Exception {
        // Given
        WithdrawRequest withdrawRequest = WithdrawRequest.builder()
                .password("Password123!")
                .build();

        CustomUserDetails userDetails = CustomUserDetails.builder()
                .email("test@example.com")
                .role(Role.ROLE_USER)
                .build();

        doNothing().when(userService).withDraw(any(WithdrawRequest.class), eq(userDetails));

        // When & Then
        mockMvc.perform(post("/users/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\": \"Password123!\"}")
                        .with(user(userDetails)))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).withDraw(any(WithdrawRequest.class), eq(userDetails));
    }

}