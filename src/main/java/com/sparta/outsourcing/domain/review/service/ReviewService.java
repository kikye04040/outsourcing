package com.sparta.outsourcing.domain.review.service;

import com.sparta.outsourcing.domain.order.entity.Order;
import com.sparta.outsourcing.domain.order.repository.OrderRepository;
import com.sparta.outsourcing.domain.review.dto.OwnerReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.OwnerReviewResponseDto;
import com.sparta.outsourcing.domain.review.entity.CustomerReview;
import com.sparta.outsourcing.domain.review.entity.OwnerReview;
import com.sparta.outsourcing.domain.review.repository.CustomerReviewRepository;
import com.sparta.outsourcing.domain.review.dto.CustomerReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.CustomerReviewResponseDto;
import com.sparta.outsourcing.domain.review.repository.OwnerReviewRepository;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.User;
import com.sparta.outsourcing.exception.BadRequestException;
import com.sparta.outsourcing.s3.ImageManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final CustomerReviewRepository customerReviewRepository;
    private final OwnerReviewRepository ownerReviewRepository;
    private final ImageManager imageManager;
    private final OrderRepository orderRepository;

    public ReviewService(CustomerReviewRepository customerReviewRepository, OwnerReviewRepository ownerReviewRepository, ImageManager imageManager, OrderRepository orderRepository) {
        this.customerReviewRepository = customerReviewRepository;
        this.ownerReviewRepository = ownerReviewRepository;
        this.imageManager = imageManager;
        this.orderRepository = orderRepository;
    }


    // 리뷰 작성
    public CustomerReviewResponseDto addReview(Long orderId, CustomerReviewRequestDto customerReviewRequestDto) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new BadRequestException("Order not found"));

        // 해당 주문에 대한 리뷰가 존재하는지
        if(customerReviewRepository.findByOrderId(orderId).isPresent()) {
            throw new BadRequestException("Is already exist");
        }

        // requestDTO에 이미지가 존재하는지
        if(!customerReviewRequestDto.getReviewPicture().isEmpty()) {
            String imageurl = imageManager.upload(customerReviewRequestDto.getReviewPicture());
            customerReviewRequestDto.setReviewPictureUrl(imageurl);
        }

        User user = order.getUser();
        Stores store = order.getStore();

        customerReviewRequestDto.setUser(user);
        customerReviewRequestDto.setStore(store);
        customerReviewRequestDto.setOrder(order);

        CustomerReview customerReview = new CustomerReview(customerReviewRequestDto);

        CustomerReview saved = customerReviewRepository.save(customerReview);

        return new CustomerReviewResponseDto(saved);
    }


    // 리뷰 조회
    public List<CustomerReviewResponseDto> getReviews(Long storeId, int minRating, int maxRating) {

        return customerReviewRepository.findByStoreIdAndRatingBetween(storeId, minRating, maxRating).stream()
                .map(CustomerReviewResponseDto::new).toList();
    }


    // 리뷰 수정
    public CustomerReviewResponseDto updateReview(CustomUserDetails customUserDetails, Long reviewId, CustomerReviewRequestDto customerReviewRequestDto) {

        CustomerReview customerReview = customerReviewRepository.findById(reviewId).orElseThrow(() -> new BadRequestException("Review not found"));

        // 작성자와 현재 로그인한 사용자가 일치하는지
        if(customUserDetails.getEmail() != customerReview.getUser().getEmail()) {
            throw new BadRequestException("Email does not match");
        }

        // requestDTO에 이미지가 존재하는지
        if(!customerReviewRequestDto.getReviewPicture().isEmpty()) {
            String imageurl = imageManager.upload(customerReviewRequestDto.getReviewPicture());
            customerReviewRequestDto.setReviewPictureUrl(imageurl);
        }

        customerReview.update(customerReviewRequestDto);

        return new CustomerReviewResponseDto(customerReview);
    }


    // 리뷰 삭제
    public String deleteReview(CustomUserDetails customUserDetails, Long reviewId) {
        CustomerReview customerReview = customerReviewRepository.findById(reviewId).orElseThrow(() -> new BadRequestException("Review not found"));

        // 작성자와 현재 로그인한 사용자가 일치하는지
        if(customUserDetails.getEmail() != customerReview.getUser().getEmail()) {
            throw new BadRequestException("Email does not match");
        }

        customerReview.softDelete();

        return "Review deleted";
    }


    /*----------------------------------------------------------------------------------------------------------------*/

    // 사장 리뷰 작성
    public OwnerReviewResponseDto addSubReview(Long reviewId, OwnerReviewRequestDto ownerReviewRequestDto) {

        CustomerReview customerReview = customerReviewRepository.findById(reviewId).orElseThrow(() -> new BadRequestException("Customer Review not found"));

        ownerReviewRequestDto.setCustomerReview(customerReview);
        OwnerReview ownerReview =  new OwnerReview(ownerReviewRequestDto);
        OwnerReview saved = ownerReviewRepository.save(ownerReview);

        return new OwnerReviewResponseDto(saved);
    }

    // 사장 리뷰 수정
    public OwnerReviewResponseDto updateSubReview(CustomUserDetails customUserDetails, Long reviewId, OwnerReviewRequestDto ownerReviewRequestDto) {

        CustomerReview customerReview = customerReviewRepository.findById(reviewId).orElseThrow(() -> new BadRequestException("Customer Review not found"));

        // 작성자와 현재 로그인한 사용자가 일치하는지
        if(customUserDetails.getEmail() != customerReview.getUser().getEmail()) {
            throw new BadRequestException("Email does not match");
        }

        OwnerReview ownerReview = customerReview.getOwnerReview();

        ownerReview.update(ownerReviewRequestDto);

        return new OwnerReviewResponseDto(ownerReview);
    }

    // 사장 리뷰 삭제
    public String deleteSubReview(CustomUserDetails customUserDetails, Long reviewId) {

        OwnerReview ownerReview = ownerReviewRepository.findById(reviewId).orElseThrow(() -> new BadRequestException("Customer Review not found"));

        // 작성자와 현재 로그인한 사용자가 일치하는지
        if(customUserDetails.getEmail() != ownerReview.getUser().getEmail()) {
            throw new BadRequestException("Email does not match");
        }

        ownerReview.softDelete();

        return "Review deleted";

    }
}
