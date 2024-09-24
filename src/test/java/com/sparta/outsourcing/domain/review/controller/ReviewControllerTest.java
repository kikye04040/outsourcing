package com.sparta.outsourcing.domain.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.outsourcing.domain.menu.repository.MenuRepository;
import com.sparta.outsourcing.domain.review.controller.ReviewController;
import com.sparta.outsourcing.domain.review.dto.CustomerReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.CustomerReviewResponseDto;
import com.sparta.outsourcing.domain.review.dto.OwnerReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.OwnerReviewResponseDto;
import com.sparta.outsourcing.domain.review.entity.CustomerReview;
import com.sparta.outsourcing.domain.review.entity.OwnerReview;
import com.sparta.outsourcing.domain.review.service.ReviewService;
import com.sparta.outsourcing.domain.stores.repository.StoresRepository;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Test
    @WithMockUser(username = "testUser")
    public void testAddReview_Success() throws Exception {
        // Given
        Long orderId = 1L;
        CustomerReviewRequestDto requestDto = new CustomerReviewRequestDto();
        requestDto.setContents("Great product!");
        requestDto.setRating(5);

        CustomerReviewResponseDto responseDto = new CustomerReviewResponseDto();
        responseDto.setId(1L);
        responseDto.setContents("Great product!");
        responseDto.setRating(5);

        when(reviewService.addReview(any(Long.class), any(CustomerReviewRequestDto.class))).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/orders/{orderId}/reviews", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(1L))
                .andExpect(jsonPath("$.content").value("Great product!"))
                .andExpect(jsonPath("$.rating").value(5))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testGetReviews_Success() throws Exception {
        // Given
        Long storeId = 1L;
        List<CustomerReviewResponseDto> reviews = List.of(
                new CustomerReviewResponseDto(new CustomerReview()),
                new CustomerReviewResponseDto(new CustomerReview().setContents())
        );

        when(reviewService.getReviews(any(Long.class), anyInt(), anyInt())).thenReturn(reviews);

        // When & Then
        mockMvc.perform(get("/stores/{storeId}/reviews", storeId)
                        .param("minRating", "1")
                        .param("maxRating", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].content").value("Great product!"))
                .andExpect(jsonPath("$[1].content").value("Not bad."))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testUpdateReview_Success() throws Exception {
        // Given
        Long storeId = 1L;
        Long reviewId = 1L;
        CustomerReviewRequestDto requestDto = new CustomerReviewRequestDto();
        requestDto.setContents("Updated review content");
        requestDto.setRating(4);

        CustomerReviewResponseDto responseDto = new CustomerReviewResponseDto();
        responseDto.setId(1L);
        responseDto.setContents("Updated review content");
        responseDto.setRating(4);

        when(reviewService.updateReview(any(CustomUserDetails.class), any(Long.class), any(CustomerReviewRequestDto.class))).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(put("/stores/{storeId}/reviews", storeId)
                        .param("reviewId", String.valueOf(reviewId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated review content"))
                .andExpect(jsonPath("$.rating").value(4))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testDeleteReview_Success() throws Exception {
        // Given
        Long storeId = 1L;
        String successMessage = "Review deleted successfully.";

        when(reviewService.deleteReview(any(CustomUserDetails.class), any(Long.class))).thenReturn(successMessage);

        // When & Then
        mockMvc.perform(put("/stores/{storeId}/reviews/delete", storeId))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage))
                .andDo(print());
    }

    // Helper method to convert objects to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}