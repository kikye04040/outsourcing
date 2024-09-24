package com.sparta.outsourcing.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.outsourcing.domain.review.controller.ReviewController;
import com.sparta.outsourcing.domain.review.dto.CustomerReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.CustomerReviewResponseDto;
import com.sparta.outsourcing.domain.review.dto.OwnerReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.OwnerReviewResponseDto;
import com.sparta.outsourcing.domain.review.entity.CustomerReview;
import com.sparta.outsourcing.domain.review.entity.OwnerReview;
import com.sparta.outsourcing.domain.review.service.ReviewService;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    ObjectMapper objectMapper;

    private CustomUserDetails customUserDetails;

    // 매번 테스트 전 CustomUserDetails 및 SecurityContext 설정
    @BeforeEach
    void setUp() {
        customUserDetails = CustomUserDetails.builder().email("test@test.com").password("password").role(Role.ROLE_USER).build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    // 리뷰 추가 성공 테스트
    @Test
    void addReview_Success() throws Exception {
        CustomerReviewRequestDto requestDto = new CustomerReviewRequestDto();
        CustomerReviewResponseDto responseDto = new CustomerReviewResponseDto(new CustomerReview());
        when(reviewService.addReview(eq(1L), any(CustomerReviewRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/orders/1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reviewService).addReview(eq(1L), any(CustomerReviewRequestDto.class));
    }

    // 가게 리뷰 조회 성공 테스트
    @Test
    void getReviews_Success() throws Exception {
        when(reviewService.getReviews(eq(1L), eq(1), eq(5))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stores/1/reviews")
                        .param("minRating", "1")
                        .param("maxRating", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reviewService).getReviews(eq(1L), eq(1), eq(5));
    }

    // 리뷰 업데이트 성공 테스트
    @Test
    void updateReview_Success() throws Exception {
        CustomerReviewRequestDto requestDto = new CustomerReviewRequestDto();
        CustomerReviewResponseDto responseDto = new CustomerReviewResponseDto(new CustomerReview());
        when(reviewService.updateReview(any(), eq(1L), any(CustomerReviewRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/stores/1/reviews")
                        .param("reviewId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        verify(reviewService).updateReview(any(CustomUserDetails.class), eq(1L), any(CustomerReviewRequestDto.class));
    }

    // 리뷰 삭제 성공 테스트
    @Test
    void deleteReview_Success() throws Exception {
        when(reviewService.deleteReview(any(CustomUserDetails.class), eq(1L))).thenReturn("Review deleted");

        mockMvc.perform(put("/stores/1/reviews/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Review deleted"));

        verify(reviewService).deleteReview(any(CustomUserDetails.class), eq(1L));
    }

    // 사장 답글 추가 성공 테스트
    @Test
    void addSubReview_Success() throws Exception {
        OwnerReviewRequestDto requestDto = new OwnerReviewRequestDto();
        OwnerReviewResponseDto responseDto = new OwnerReviewResponseDto(new OwnerReview());
        when(reviewService.addSubReview(eq(1L), any(OwnerReviewRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/stores/1/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        verify(reviewService).addSubReview(eq(1L), any(OwnerReviewRequestDto.class));
    }

    // 사장 답글 수정 성공 테스트
    @Test
    void updateSubReview_Success() throws Exception {
        OwnerReviewRequestDto requestDto = new OwnerReviewRequestDto();
        OwnerReviewResponseDto responseDto = new OwnerReviewResponseDto(new OwnerReview());
        when(reviewService.updateSubReview(any(CustomUserDetails.class), eq(1L), any(OwnerReviewRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/stores/1/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        verify(reviewService).updateSubReview(any(CustomUserDetails.class), eq(1L), any(OwnerReviewRequestDto.class));
    }

    // 사장 답글 삭제 성공 테스트
    @Test
    void deleteSubReview_Success() throws Exception {
        when(reviewService.deleteSubReview(any(CustomUserDetails.class), eq(1L))).thenReturn("Review deleted");

        mockMvc.perform(put("/stores/1/reviews/1/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Review deleted"));

        verify(reviewService).deleteSubReview(any(CustomUserDetails.class), eq(1L));
    }
}
