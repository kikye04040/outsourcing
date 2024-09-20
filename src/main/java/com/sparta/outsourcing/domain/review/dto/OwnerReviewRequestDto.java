package com.sparta.outsourcing.domain.review.dto;

import com.sparta.outsourcing.domain.order.entity.Order;
import com.sparta.outsourcing.domain.review.entity.CustomerReview;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerReviewRequestDto {

    private Long ownerReviewId;
    private String contents;
    private String status = "existed";

    private CustomerReview customerReview;
}
