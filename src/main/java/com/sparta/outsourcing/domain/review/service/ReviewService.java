package com.sparta.outsourcing.domain.review.service;

import com.sparta.outsourcing.domain.review.dto.OwnerReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.OwnerReviewResponseDto;
import com.sparta.outsourcing.domain.review.entity.Review;
import com.sparta.outsourcing.domain.review.repository.ReviewRepository;
import com.sparta.outsourcing.domain.review.dto.ReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.ReviewResponseDto;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    // 리뷰 작성
    public ReviewResponseDto addReview(ReviewRequestDto reviewRequestDto) {
        Review review = new Review(reviewRequestDto);
        Review saved = reviewRepository.save(review);

        return new ReviewResponseDto(saved);
    }


    // 리뷰 조회
    public List<ReviewResponseDto> getReviews(Long storeId, int minRating, int maxRating) {

        return reviewRepository.findByStoreIdAndRatingBetween(storeId, minRating, maxRating).stream()
                .map(ReviewResponseDto::new).toList();
    }


    // 리뷰 수정
    public ReviewResponseDto updateReview(CustomUserDetails customUserDetails, Long reviewId, ReviewRequestDto reviewRequestDto) {

        Review review = reviewRepository.findById(reviewId).orElseThrow();

        if(customUserDetails.getEmail() != review.getUser().getEmail()) {
            // 예외처리 진행해야함
            return null;
        }

        review.update(reviewRequestDto);

        return new ReviewResponseDto(review);
    }


    // 리뷰 삭제
    public String deleteReview(CustomUserDetails customUserDetails, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();

        if(customUserDetails.getEmail() != review.getUser().getEmail()) {
            // 예외처리 진행해야함
            return null;
        }

        reviewRepository.delete(review);

        return "Review deleted";
    }


    // 사장 리뷰 작성
    public OwnerReviewResponseDto addSubReview(Long reviewId, OwnerReviewRequestDto ownerReviewRequestDto) {

        Review customerReview = reviewRepository.findById(reviewId).orElseThrow();

        Review review = new Review(ownerReviewRequestDto);
        Review saved = reviewRepository.save(review);

        return new OwnerReviewResponseDto(saved);
    }

    // 사장 리뷰 수정
    public OwnerReviewResponseDto updateSubReview(CustomUserDetails customUserDetails, Long reviewId, OwnerReviewRequestDto ownerReviewRequestDto) {

        Review review = reviewRepository.findById(reviewId).orElseThrow();

        if(customUserDetails.getEmail() != review.getUser().getEmail()) {
            // 예외처리 진행해야함
            return null;
        }

        review.update(ownerReviewRequestDto);

        return new OwnerReviewResponseDto(review);
    }

    public String deleteSubReview(CustomUserDetails customUserDetails, Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow();

        if(customUserDetails.getEmail() != review.getUser().getEmail()) {
            // 예외처리 진행해야함
            return null;
        }

        reviewRepository.delete(review);

        return "Review deleted";

    }
}
