package com.sparta.outsourcing.domain.review.dto;

import com.sparta.outsourcing.domain.review.entity.CustomerReview;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerReviewResponseDto {

    private Long id;
    private Long orderId;
    private String contents;
    private int rating;
    private Long userId;
    private Long storeId;

    public CustomerReviewResponseDto(CustomerReview customerReview) {
        this.id = customerReview.getId();
        this.orderId = customerReview.getOrder().getId();
        this.contents = customerReview.getContents();
        this.rating = customerReview.getRating();
        this.userId = customerReview.getUser().getId();

    }
}
