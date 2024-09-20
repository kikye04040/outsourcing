package com.sparta.outsourcing.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
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

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = jwtUtil.getTokenFromRequest(request);

        if (!StringUtils.hasText(tokenValue)) {
            log.info("TOKEN_NULL");
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtUtil.substringToken(tokenValue);

        if (!jwtUtil.validateToken(token)) {
            log.info("TOKEN_INVALID");
            filterChain.doFilter(request, response);
            return;
        }

        CustomUserDetails customUserDetails = jwtUtil.getCustomUserDetailsFromToken(token);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        String path = request.getRequestURI();
//        return path.equals("/login") ||
//                path.equals("/") ||
//                path.equals("/join") ||
//                path.startsWith("/stores") ||
//                path.startsWith("/reviews") ||
//                path.startsWith("/orders") ||
//                path.startsWith("/users/") && !path.equals("/users/me");
//    }

//    private void setErrorResponse(
//            HttpServletResponse response,
//            ErrorCode errorCode
//    ) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        response.setStatus(errorCode.getHttpStatus().value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding("UTF-8");
//        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
//        try {
//            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Data
//    public static class ErrorResponse {
//        private final Integer code;
//        private final String message;
//    }
}
