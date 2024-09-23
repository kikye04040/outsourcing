package com.sparta.outsourcing.domain.review.controller;

import com.sparta.outsourcing.domain.review.dto.OwnerReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.OwnerReviewResponseDto;
import com.sparta.outsourcing.domain.review.dto.CustomerReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.CustomerReviewResponseDto;
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
    public ResponseEntity<?> addReview(@RequestBody CustomerReviewRequestDto customerReviewRequestDto) {
        CustomerReviewResponseDto customerReviewResponseDto = reviewService.addReview(customerReviewRequestDto);

        return ResponseEntity.ok(customerReviewResponseDto);
    }


    // 해당 가게의 리뷰 조회
    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<?> getReviews(@PathVariable Long storeId,
                                        @RequestParam(value = "minRating", defaultValue = "1") int minRating,
                                        @RequestParam(value = "maxRating", defaultValue = "5") int maxRating) {

        List<CustomerReviewResponseDto> reviews = reviewService.getReviews(storeId, minRating, maxRating);

        return ResponseEntity.ok(reviews);
    }


    // 작성한 리뷰 수정
    @PutMapping("/stores/{storeId}/reviews")
    public ResponseEntity<?> updateReview(@RequestParam(value = "reviewId") Long reviewId,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                          @RequestBody CustomerReviewRequestDto customerReviewRequestDto) {

        CustomerReviewResponseDto customerReviewResponseDto = reviewService.updateReview(customUserDetails, reviewId, customerReviewRequestDto);

        return ResponseEntity.ok(customerReviewResponseDto);
    }


    // 작성한 리뷰 삭제
    @PutMapping("/stores/{storeId}/reviews/delete")
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
    @PutMapping("/stores/{storeId}/reviews/{reviewId}/delete")
    public ResponseEntity<?> deleteSubReview(@PathVariable(name = "reviewId") Long reviewId,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        String msg = reviewService.deleteSubReview(customUserDetails, reviewId);
        return ResponseEntity.ok(msg);
    }

}
