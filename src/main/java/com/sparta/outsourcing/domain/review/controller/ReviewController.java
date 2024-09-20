package com.sparta.outsourcing.domain.review.controller;

import com.sparta.outsourcing.domain.review.dto.OwnerReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.OwnerReviewResponseDto;
import com.sparta.outsourcing.domain.review.dto.ReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.ReviewResponseDto;
import com.sparta.outsourcing.domain.review.entity.Review;
import com.sparta.outsourcing.domain.review.service.ReviewService;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping("/reviews")
    public ResponseEntity<?> addReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        ReviewResponseDto reviewResponseDto = reviewService.addReview(reviewRequestDto);

        return ResponseEntity.ok(reviewResponseDto);
    }


    // 해당 가게의 리뷰 조회
    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<?> getReviews(@PathVariable Long storeId,
                                        @RequestParam(value = "minRating", defaultValue = "1") int minRating,
                                        @RequestParam(value = "maxRating", defaultValue = "5") int maxRating) {

        List<ReviewResponseDto> reviews = reviewService.getReviews(storeId, minRating, maxRating);

        return ResponseEntity.ok(reviews);
    }


    // 작성한 리뷰 수정
    @PutMapping("/stores/{storeId}/reviews")
    public ResponseEntity<?> updateReview(@RequestParam(value = "reviewId") Long reviewId,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                          @RequestBody ReviewRequestDto reviewRequestDto) {

        ReviewResponseDto reviewResponseDto = reviewService.updateReview(customUserDetails, reviewId, reviewRequestDto);

        return ResponseEntity.ok(reviewResponseDto);
    }


    // 작성한 리뷰 삭제
    @PutMapping("/stores/{storeId}/reviews")
    public ResponseEntity<?> deleteReview(@PathVariable Long storeId,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        String msg = reviewService.deleteReview(customUserDetails, storeId);

        return ResponseEntity.ok(msg);
    }



    // 사장 리뷰 작성
    @PostMapping("/stores/{storeId}/reviews/{reviewId}")
    public ResponseEntity<?> addSubReview(@PathVariable(name = "reviewId") Long reviewId,
                                          @RequestBody OwnerReviewRequestDto ownerReviewRequestDto) {

        OwnerReviewResponseDto ownerReviewResponseDto = reviewService.addSubReview(reviewId, ownerReviewRequestDto);
        return ResponseEntity.ok("");
    }


    // 사장 리뷰 수정
    @PutMapping("/stores/{storeId}/reviews/{reviewId}")
    public ResponseEntity<?> updateSubReview(@PathVariable(name = "reviewId") Long reviewId,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @RequestBody OwnerReviewRequestDto ownerReviewRequestDto) {
        OwnerReviewResponseDto ownerReviewResponseDto = reviewService.updateSubReview(customUserDetails, reviewId, ownerReviewRequestDto);
        return ResponseEntity.ok("");
    }


    // 사장 리뷰 삭제
    @PutMapping("/stores/{storeId}/reviews/{reviewId}")
    public ResponseEntity<?> deleteSubReview(@PathVariable(name = "reviewId") Long reviewId,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        String msg = reviewService.deleteSubReview(customUserDetails, reviewId);
        return ResponseEntity.ok(msg);
    }



}
