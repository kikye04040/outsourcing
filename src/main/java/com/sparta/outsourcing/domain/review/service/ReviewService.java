package com.sparta.outsourcing.domain.review.service;

import com.sparta.outsourcing.domain.review.dto.OwnerReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.OwnerReviewResponseDto;
import com.sparta.outsourcing.domain.review.entity.CustomerReview;
import com.sparta.outsourcing.domain.review.entity.OwnerReview;
import com.sparta.outsourcing.domain.review.repository.CustomerReviewRepository;
import com.sparta.outsourcing.domain.review.dto.CustomerReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.CustomerReviewResponseDto;
import com.sparta.outsourcing.domain.review.repository.OwnerReviewRepository;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final CustomerReviewRepository customerReviewRepository;
    private final OwnerReviewRepository ownerReviewRepository;

    public ReviewService(CustomerReviewRepository customerReviewRepository, OwnerReviewRepository ownerReviewRepository) {
        this.customerReviewRepository = customerReviewRepository;
        this.ownerReviewRepository = ownerReviewRepository;
    }


    // 리뷰 작성
    public CustomerReviewResponseDto addReview(CustomerReviewRequestDto customerReviewRequestDto) {
        CustomerReview customerReview = new CustomerReview(customerReviewRequestDto);
        CustomerReview saved = customerReviewRepository.save(customerReview);

        return new CustomerReviewResponseDto(saved);
    }


    // 리뷰 조회
    public List<CustomerReviewResponseDto> getReviews(Long storeId, int minRating, int maxRating) {

        return customerReviewRepository.findByStoreIdAndRatingBetween(storeId, minRating, maxRating).stream()
                .map(CustomerReviewResponseDto::new).toList();
    }


    // 리뷰 수정
    public CustomerReviewResponseDto updateReview(CustomUserDetails customUserDetails, Long reviewId, CustomerReviewRequestDto customerReviewRequestDto) {

        CustomerReview customerReview = customerReviewRepository.findById(reviewId).orElseThrow();

        if(customUserDetails.getEmail() != customerReview.getUser().getEmail()) {
            // 예외처리 진행해야함
            return null;
        }

        customerReview.update(customerReviewRequestDto);

        return new CustomerReviewResponseDto(customerReview);
    }


    // 리뷰 삭제
    public String deleteReview(CustomUserDetails customUserDetails, Long reviewId) {
        CustomerReview customerReview = customerReviewRepository.findById(reviewId).orElseThrow();

        if(customUserDetails.getEmail() != customerReview.getUser().getEmail()) {
            // 예외처리 진행해야함
            return null;
        }

        customerReview.softDelete();

        return "Review deleted";
    }


    /*----------------------------------------------------------------------------------------------------------------*/

    // 사장 리뷰 작성
    public OwnerReviewResponseDto addSubReview(Long reviewId, OwnerReviewRequestDto ownerReviewRequestDto) {

        CustomerReview customerReview = customerReviewRepository.findById(reviewId).orElseThrow();

        ownerReviewRequestDto.setCustomerReview(customerReview);
        OwnerReview ownerReview =  new OwnerReview(ownerReviewRequestDto);
        OwnerReview saved = ownerReviewRepository.save(ownerReview);

        return new OwnerReviewResponseDto(saved);
    }

    // 사장 리뷰 수정
    public OwnerReviewResponseDto updateSubReview(CustomUserDetails customUserDetails, Long reviewId, OwnerReviewRequestDto ownerReviewRequestDto) {

        CustomerReview customerReview = customerReviewRepository.findById(reviewId).orElseThrow();

        if(customUserDetails.getEmail() != customerReview.getUser().getEmail()) {
            // 예외처리 진행해야함
            return null;
        }

        OwnerReview ownerReview = customerReview.getOwnerReview();

        ownerReview.update(ownerReviewRequestDto);

        return new OwnerReviewResponseDto(ownerReview);
    }

    public String deleteSubReview(CustomUserDetails customUserDetails, Long reviewId) {

        OwnerReview ownerReview = ownerReviewRepository.findById(reviewId).orElseThrow();

        if(customUserDetails.getEmail() != ownerReview.getUser().getEmail()) {
            // 예외처리 진행해야함
            return null;
        }

        ownerReview.softDelete();

        return "Review deleted";

    }
}
