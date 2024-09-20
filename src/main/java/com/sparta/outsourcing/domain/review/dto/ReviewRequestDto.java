package com.sparta.outsourcing.domain.review.dto;

import com.sparta.outsourcing.domain.order.entity.Order;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.user.entity.User;
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

    private User user;
    private Stores store;
    private Order order;
}
