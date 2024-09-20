package com.sparta.outsourcing.domain.review.repository;

import com.sparta.outsourcing.domain.review.entity.CustomerReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerReviewRepository extends JpaRepository<CustomerReview, Long> {

    List<CustomerReview> findByStore_StoreIdAndRatingBetween(Long storeId, Integer minRating, Integer maxRating);
}
