package com.sparta.outsourcing.domain.review.repository;

import com.sparta.outsourcing.domain.review.entity.CustomerReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerReviewRepository extends JpaRepository<CustomerReview, Long> {

    List<CustomerReview> findByStoreIdAndRatingBetween(Long storeId, Integer minRating, Integer maxRating);
    Optional<CustomerReview> findByOrderId(Long orderId);
}
