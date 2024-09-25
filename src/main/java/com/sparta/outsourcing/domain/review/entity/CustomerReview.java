package com.sparta.outsourcing.domain.review.entity;

import com.sparta.outsourcing.domain.order.entity.Order;
import com.sparta.outsourcing.domain.review.StatusType;
import com.sparta.outsourcing.domain.review.dto.CustomerReviewRequestDto;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CustomerReview")
public class CustomerReview extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String contents;

    @Column
    private String reviewPictureUrl;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    @Enumerated(EnumType.STRING)  // Enum을 문자열로 저장
    @Column(nullable = false)
    private StatusType status;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "storeId", nullable = false)
    private Stores store;

    @OneToOne(mappedBy = "customerReview", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private OwnerReview ownerReview;


    public CustomerReview(CustomerReviewRequestDto customerReviewRequestDto) {
        this.rating = customerReviewRequestDto.getRating();
        this.contents = customerReviewRequestDto.getContents();
        this.status = StatusType.ACTIVATE;
        this.order = customerReviewRequestDto.getOrder();
        this.user = customerReviewRequestDto.getUser();
        this.store = customerReviewRequestDto.getStore();
        this.reviewPictureUrl = customerReviewRequestDto.getReviewPictureUrl();
    }

    public void update(CustomerReviewRequestDto customerReviewRequestDto) {
        this.rating = customerReviewRequestDto.getRating();
        this.contents = customerReviewRequestDto.getContents();
        this.reviewPictureUrl = customerReviewRequestDto.getReviewPictureUrl();
    }

    public void softDelete() {
        this.status = StatusType.DELETED;
    }

}
