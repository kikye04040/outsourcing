package com.sparta.outsourcing.domain.review.repository;

import com.sparta.outsourcing.domain.review.entity.OwnerReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerReviewRepository extends JpaRepository<OwnerReview, Long> {
}
