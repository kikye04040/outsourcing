package com.sparta.outsourcing.domain.stores.service;

import com.sparta.outsourcing.domain.stores.dto.StoreResponseDto;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.stores.entity.StoresLike;
import com.sparta.outsourcing.domain.stores.repository.StoresLikeRepository;
import com.sparta.outsourcing.domain.stores.repository.StoresRepository;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.domain.user.entity.User;
import com.sparta.outsourcing.domain.user.repository.UserRepository;
import com.sparta.outsourcing.exception.ForbiddenException;
import com.sparta.outsourcing.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StoresLikeServiceTest {

    @Mock
    private StoresRepository storesRepository;

    @Mock
    private StoresLikeRepository storesLikeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StoresLikeService storesLikeService;

    private User mockUser;
    private Stores mockStore;
    private StoresLike mockStoreLike;
    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock User
        mockUser = User.builder()
            .email("owner@example.com")
            .name("owner")
            .build();

        // Mock Store
        mockStore = new Stores(
            "Test Store",
            1,
            "Category",
            "Address",
            "010-1234-5678",
            "Contents",
            "picture.jpg",
            1000,
            "09:00~22:00",
            "SUNDAY",
            mockUser,
            5000);
        ReflectionTestUtils.setField(mockStore, "id", 1L); // 가게 ID 설정

        // Mock StoreLike
        mockStoreLike = new StoresLike(mockUser, mockStore);

        // Mock CustomUserDetails
        userDetails = mock(CustomUserDetails.class);
        when(userDetails.getEmail()).thenReturn("test@example.com");
        when(userDetails.getRole()).thenReturn(Role.ROLE_USER);
    }

    @Test
    void 좋아요_성공() {
        // given
        when(storesRepository.findById(anyLong())).thenReturn(Optional.of(mockStore));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(storesLikeRepository.findByUserAndStores(any(User.class), any(Stores.class))).thenReturn(Optional.empty());

        // when
        StoreResponseDto response = storesLikeService.StoreLike(1L, userDetails);

        // then
        assertEquals("Store liked successfully", response.getMessage());
        verify(storesLikeRepository, times(1)).save(any(StoresLike.class));
    }

    @Test
    void 좋아요_취소_성공() {
        // given
        when(storesRepository.findById(anyLong())).thenReturn(Optional.of(mockStore));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(storesLikeRepository.findByUserAndStores(any(User.class), any(Stores.class))).thenReturn(Optional.of(mockStoreLike));

        // when
        StoreResponseDto response = storesLikeService.StoreLike(1L, userDetails);

        // then
        assertEquals("Store unliked successfully", response.getMessage());
        verify(storesLikeRepository, times(1)).delete(any(StoresLike.class));
    }

    @Test
    void 좋아요_가게_없음() {
        // given
        when(storesRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        Exception exception = assertThrows(NotFoundException.class, () -> storesLikeService.StoreLike(1L, userDetails));
        assertEquals("Store not found", exception.getMessage());
    }

    @Test
    void 좋아요_사용자_없음() {
        // given
        when(storesRepository.findById(anyLong())).thenReturn(Optional.of(mockStore));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when & then
        Exception exception = assertThrows(NotFoundException.class, () -> storesLikeService.StoreLike(1L, userDetails));
        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void 좋아요_권한_없음() {
        // given
        when(userDetails.getRole()).thenReturn(Role.ROLE_ADMIN); // ROLE_USER가 아닌 경우

        // when & then
        Exception exception = assertThrows(ForbiddenException.class, () -> storesLikeService.StoreLike(1L, userDetails));
        assertEquals("사용자 계정으로만 좋아요가 가능합니다.", exception.getMessage());
    }
}
