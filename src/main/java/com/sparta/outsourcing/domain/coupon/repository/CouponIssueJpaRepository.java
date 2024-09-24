package com.sparta.outsourcing.domain.coupon.repository;

import com.sparta.outsourcing.domain.coupon.entity.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue, Long> {

    @Query("SELECT ci FROM CouponIssue ci WHERE ci.couponId = :couponId AND ci.email = :email")
    Optional<CouponIssue> findCouponIssue(long couponId, String email);
}
