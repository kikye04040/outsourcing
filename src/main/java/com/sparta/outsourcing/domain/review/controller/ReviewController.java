package com.sparta.outsourcing.domain.review.controller;

import com.sparta.outsourcing.domain.review.reviewDTO.ReviewRequestDto;
import com.sparta.outsourcing.domain.review.reviewDTO.ReviewResponseDto;
import com.sparta.outsourcing.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<?> addReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        ReviewResponseDto reviewResponseDto = reviewService.addReview(reviewRequestDto);

        return ResponseEntity.ok(reviewResponseDto);
    }

    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<?> getReviews(@PathVariable Long storeId,
                                        @RequestParam(value = "minRating", defaultValue = "1") int minRating,
                                        @RequestParam(value = "maxRating", defaultValue = "5") int maxRating) {

        List<ReviewResponseDto> reviews = reviewService.getReviews(storeId, minRating, maxRating);

        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/stores/{storeId}/reviews")
    public ResponseEntity<?> updateReview(@RequestParam(value = "reviewId") Long reviewId,
                                          @RequestBody ReviewRequestDto reviewRequestDto) {

        ReviewResponseDto reviewResponseDto = reviewService.updateReview(userId, reviewId, reviewRequestDto);

        return ResponseEntity.ok(reviewResponseDto);
    }

    @DeleteMapping("/stores/{storeId}/reviews")
    public ResponseEntity<?> deleteReview(@PathVariable Long storeId) {

        String msg = reviewService.deleteReview(userId, storeId);

        return ResponseEntity.ok(msg);
    }
}
