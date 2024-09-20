package com.sparta.outsourcing.domain.review.dto;

import com.sparta.outsourcing.domain.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerReviewResponseDto {

    private Long id;
    private Long orderId;
    private String contents;
    private int rating;
    private Long userId;
    private Long storeId;

    public OwnerReviewResponseDto(Review review) {
        this.id = review.getId();
        this.orderId = review.getOrder().getId();
        this.contents = review.getContents();
        this.rating = review.getRating();
        this.userId = review.getUser().getId();
        this.storeId = review.getStore().getStoreId();

    }
}
