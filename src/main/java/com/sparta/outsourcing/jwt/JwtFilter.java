package com.sparta.outsourcing.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = jwtUtil.getTokenFromRequest(request);

        // 카카오 로그인 로직
        String requestURI = request.getRequestURI();
        // 특정 경로는 필터를 적용하지 않음
        if (requestURI.equals("/login") || requestURI.equals("/callback") || requestURI.equals("/favicon.ico")) {
            filterChain.doFilter(request, response); // 다음 필터로 이동
            return; // 필터 중단
        }

        if (!StringUtils.hasText(tokenValue)) {
            log.info("TOKEN_NULL");
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtUtil.substringToken(tokenValue);

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {

            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (!jwtUtil.validateToken(token)) {
            log.info("TOKEN_INVALID");
            filterChain.doFilter(request, response);
            return;
        }

        String category = jwtUtil.getCategory(token);

        if (!category.equals("access")) {

            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        CustomUserDetails customUserDetails = jwtUtil.getCustomUserDetailsFromToken(token);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
