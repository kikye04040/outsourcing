package com.sparta.outsourcing.domain.review.entity;

import com.sparta.outsourcing.domain.review.StatusType;
import com.sparta.outsourcing.domain.review.dto.OwnerReviewRequestDto;
import com.sparta.outsourcing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "status <> 'DELETED'")
@Table
public class OwnerReview extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @Enumerated(EnumType.STRING)  // Enum을 문자열로 저장
    @Column(nullable = false)
    private StatusType status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_review_id", nullable = false)
    private CustomerReview customerReview;


    public OwnerReview(OwnerReviewRequestDto ownerReviewRequestDto) {
        this.contents = ownerReviewRequestDto.getContents();
        this.status = StatusType.ACTIVATE;
    }

    public void update(OwnerReviewRequestDto ownerReviewRequestDto) {
        this.contents = ownerReviewRequestDto.getContents();
    }

    public void softDelete() {
        this.status = StatusType.DELETED;
    }
}
