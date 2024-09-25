package com.sparta.outsourcing.domain.user.controller;

import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.dto.request.WithdrawRequest;
import com.sparta.outsourcing.domain.user.dto.response.UserListResponse;
import com.sparta.outsourcing.domain.user.dto.response.UserResponse;
import com.sparta.outsourcing.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse userResponse = userService.findByEmail(userDetails.getEmail());
        return ResponseEntity.ok(userResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        UserResponse userResponse = userService.findById(userId);
        return ResponseEntity.ok(userResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<UserListResponse> getUsers() {
        UserListResponse userListResponse = userService.findAll();
        return ResponseEntity.ok(userListResponse);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody WithdrawRequest withdrawRequest,
                                         @AuthenticationPrincipal CustomUserDetails userDetails,
                                         HttpServletResponse response) {

        userService.withDraw(withdrawRequest, userDetails);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }
}
