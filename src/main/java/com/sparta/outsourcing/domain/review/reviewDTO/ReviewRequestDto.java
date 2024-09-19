package com.sparta.outsourcing.domain.review.reviewDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {

    private Long reviewId;
    private Long orderId;
    private int rating;
    private String contents;
    private String reviewPictureUrl;
}
