package com.sparta.outsourcing.review.repository;

import com.sparta.outsourcing.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByStoreIdAndRatingBetween(Long storeId, Integer minRating, Integer maxRating);
}
