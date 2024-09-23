package com.sparta.outsourcing.review.service;

import com.sparta.outsourcing.domain.order.entity.Order;
import com.sparta.outsourcing.domain.review.dto.CustomerReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.CustomerReviewResponseDto;
import com.sparta.outsourcing.domain.review.dto.OwnerReviewRequestDto;
import com.sparta.outsourcing.domain.review.dto.OwnerReviewResponseDto;
import com.sparta.outsourcing.domain.review.entity.CustomerReview;
import com.sparta.outsourcing.domain.review.entity.OwnerReview;
import com.sparta.outsourcing.domain.review.repository.CustomerReviewRepository;
import com.sparta.outsourcing.domain.review.repository.OwnerReviewRepository;
import com.sparta.outsourcing.domain.review.service.ReviewService;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.Grade;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.domain.user.entity.Status;
import com.sparta.outsourcing.domain.user.entity.User;
import com.sparta.outsourcing.s3.ImageManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private CustomerReviewRepository customerReviewRepository;

    @Mock
    private OwnerReviewRepository ownerReviewRepository;

    @Mock
    private ImageManager imageManager;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void addReview_Success() {
        // given
        CustomerReviewRequestDto requestDto = new CustomerReviewRequestDto();
        User user = TestUtil.createMockUser();
        Stores store = TestUtil.createMockStores(user);
        Order order = TestUtil.createMockOrder(user, store);
        order.setId(1L);
        requestDto.setOrder(order);
        requestDto.setUser(user);
        requestDto.setStore(store);

        // 가짜 이미지 파일 생성
        MockMultipartFile mockFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", "image-data".getBytes());
        requestDto.setReviewPicture(mockFile);

        CustomerReview savedReview = new CustomerReview();
        savedReview.setOrder(order);
        savedReview.setUser(user);
        savedReview.setStore(store);

        when(imageManager.upload(any(MultipartFile.class))).thenReturn("image-url");
        when(customerReviewRepository.save(any(CustomerReview.class))).thenReturn(savedReview);

        // when
        CustomerReviewResponseDto responseDto = reviewService.addReview(requestDto);

        // then
        assertNotNull(responseDto);
        verify(customerReviewRepository).save(any(CustomerReview.class));
        verify(imageManager).upload(mockFile);
    }

    /*@Test
    void getReviews_Success() {
        // given
        Long storeId = 1L;
        int minRating = 3;
        int maxRating = 5;
        List<CustomerReview> reviews = List.of(new CustomerReview(), new CustomerReview());

        when(customerReviewRepository.findByStoreIdAndRatingBetween(storeId, minRating, maxRating)).thenReturn(reviews);

        // when
        List<CustomerReviewResponseDto> result = reviewService.getReviews(storeId, minRating, maxRating);

        // then
        assertEquals(2, result.size());
        verify(customerReviewRepository).findByStoreIdAndRatingBetween(storeId, minRating, maxRating);
    }*/

    @Test
    void updateReview_Success() {
        // given
        Long reviewId = 1L;
        CustomUserDetails customUserDetails = CustomUserDetails.builder().email("email").password("password").name("name").role(Role.ROLE_USER).build();
        CustomerReviewRequestDto requestDto = new CustomerReviewRequestDto();

        User user = TestUtil.createMockUser();
        Stores store = TestUtil.createMockStores(user);
        Order order = TestUtil.createMockOrder(user, store);
        order.setId(1L);


        // 가짜 이미지 파일 생성
        MockMultipartFile mockFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", "image-data".getBytes());
        requestDto.setReviewPicture(mockFile);

        CustomerReview review = new CustomerReview();
        review.setUser(user);
        review.setStore(store);
        review.setOrder(order);

        when(customerReviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(imageManager.upload(any(MultipartFile.class))).thenReturn("image-url");

        // when
        CustomerReviewResponseDto responseDto = reviewService.updateReview(customUserDetails, reviewId, requestDto);

        // then
        assertNotNull(responseDto);
        verify(customerReviewRepository).findById(reviewId);
        verify(imageManager).upload(mockFile);
    }

    @Test
    void deleteReview_Success() {
        // given
        Long reviewId = 1L;
        CustomUserDetails customUserDetails = CustomUserDetails.builder().email("email").password("password").name("name").role(Role.ROLE_USER).build();
        CustomerReview review = new CustomerReview();

        // Mock User, Stores, Order
        User user = TestUtil.createMockUser();
        Stores store = TestUtil.createMockStores(user);
        Order order = TestUtil.createMockOrder(user, store);
        order.setId(1L);

        review.setUser(user);
        review.setStore(store);
        review.setOrder(order);

        when(customerReviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // when
        String result = reviewService.deleteReview(customUserDetails, reviewId);

        // then
        assertEquals("Review deleted", result);
        verify(customerReviewRepository).findById(reviewId);
    }


    @Test
    void addSubReview_Success() {
        // given
        Long reviewId = 1L;
        OwnerReviewRequestDto requestDto = new OwnerReviewRequestDto();

        // Mock User, Stores, Order
        User user = TestUtil.createMockUser();
        Stores store = TestUtil.createMockStores(user);
        Order order = TestUtil.createMockOrder(user, store);

        CustomerReview customerReview = new CustomerReview();
        customerReview.setId(reviewId);
        customerReview.setUser(user);
        customerReview.setStore(store);
        customerReview.setOrder(order);  // Setting Order in CustomerReview

        when(customerReviewRepository.findById(reviewId)).thenReturn(Optional.of(customerReview));

        OwnerReview ownerReview = new OwnerReview();
        ownerReview.setCustomerReview(customerReview);
        when(ownerReviewRepository.save(any(OwnerReview.class))).thenReturn(ownerReview);

        // when
        OwnerReviewResponseDto responseDto = reviewService.addSubReview(reviewId, requestDto);

        // then
        assertNotNull(responseDto);
        verify(ownerReviewRepository).save(any(OwnerReview.class));
    }

    @Test
    void updateSubReview_Success() {
        // given
        Long reviewId = 1L;
        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .email("email")
                .password("password")
                .name("name")
                .role(Role.ROLE_USER)
                .build();

        // Mock User, Stores, Order
        User user = TestUtil.createMockUser();
        Stores store = TestUtil.createMockStores(user);
        Order order = TestUtil.createMockOrder(user, store);
        order.setId(1L);

        OwnerReviewRequestDto requestDto = new OwnerReviewRequestDto();
        CustomerReview customerReview = new CustomerReview();
        customerReview.setUser(user);
        customerReview.setStore(store);
        customerReview.setOrder(order);  // Setting Order in CustomerReview

        OwnerReview ownerReview = new OwnerReview();
        customerReview.setOwnerReview(ownerReview);

        when(customerReviewRepository.findById(reviewId)).thenReturn(Optional.of(customerReview));

        // when
        OwnerReviewResponseDto responseDto = reviewService.updateSubReview(customUserDetails, reviewId, requestDto);

        // then
        assertNotNull(responseDto);
        verify(customerReviewRepository).findById(reviewId);
    }

    @Test
    void deleteSubReview_Success() {
        // given
        Long reviewId = 1L;
        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .email("email")
                .password("password")
                .name("name")
                .role(Role.ROLE_OWNER)
                .build();

        // Mock User, Stores, Order
        User user = TestUtil.createMockUser();
        Stores store = TestUtil.createMockStores(user);
        Order order = TestUtil.createMockOrder(user, store);
        order.setId(1L);

        OwnerReview ownerReview = new OwnerReview();
        ownerReview.setUser(user);

        when(ownerReviewRepository.findById(reviewId)).thenReturn(Optional.of(ownerReview));

        // when
        String result = reviewService.deleteSubReview(customUserDetails, reviewId);

        // then
        assertEquals("Review deleted", result);
        verify(ownerReviewRepository).findById(reviewId);
    }
}
