package com.sparta.outsourcing.domain.review.dto;

import com.sparta.outsourcing.domain.review.entity.CustomerReview;
import com.sparta.outsourcing.domain.review.entity.OwnerReview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OwnerReviewResponseDto {

    private Long id;
    private Long customerReviewId;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public OwnerReviewResponseDto(OwnerReview ownerReview) {
        this.id = ownerReview.getId();
        this.customerReviewId = ownerReview.getCustomerReview().getId();
        this.contents = ownerReview.getContents();
        this.createdAt = ownerReview.getCreatedAt();
        this.modifiedAt = ownerReview.getModifiedAt();

    }
}
