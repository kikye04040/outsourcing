package com.sparta.outsourcing.review.entity;

import com.sparta.outsourcing.review.reviewDTO.ReviewRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int rating;

    @Column
    private String contents;

    @Column
    private String reviewPictureUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private String status;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId", nullable = false)
    private Store store;


    public Review(ReviewRequestDto reviewRequestDto) {
        this.rating = reviewRequestDto.getRating();
        this.contents = reviewRequestDto.getContents();
        this.order = reviewRequestDto.getOrder();
        this.user = reviewRequestDto.getUser();
        this.store = reviewRequestDto.getStore();
    }

    public void update(ReviewRequestDto reviewRequestDto) {
        this.rating = reviewRequestDto.getRating();
        this.contents = reviewRequestDto.getContents();
        this.reviewPictureUrl = reviewRequestDto.getReviewPictureUrl();
    }

}
