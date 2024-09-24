package com.sparta.outsourcing.domain.stores.service;

import com.sparta.outsourcing.domain.stores.dto.*;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.stores.repository.StoresRepository;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.domain.user.entity.User;
import com.sparta.outsourcing.domain.user.repository.UserRepository;
import com.sun.jdi.request.InvalidRequestStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StoresServiceTest {

    @Mock
    private StoresRepository storesRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StoresService storesService;

    private User mockUser;
    private Stores mockStore;
    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 사용자 설정
        mockUser = User.builder()
            .email("owner@example.com")
            .name("owner")
            .build();

        // 가게 생성 및 ID 설정
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

        // CustomUserDetails 모킹
        userDetails = mock(CustomUserDetails.class);
        when(userDetails.getEmail()).thenReturn("owner@example.com");
        when(userDetails.getRole()).thenReturn(Role.ROLE_OWNER);
    }

    @Test
    void 가게_생성_성공 () {
        // given
        StoreCreatedRequestDto requestDto = new StoreCreatedRequestDto(
            "New Store",
            2,
            "Category",
            "Address",
            "010-1234-5678",
            "Contents",
            "picture.jpg",
            1000,
            "09:00~22:00",
            "SUNDAY",
            5000);

        // 가게 수 체크 및 유저 모킹
        when(storesService.getStoreCountByUser(any())).thenReturn(2);
        when(userRepository.findByEmailOrElseThrow(any())).thenReturn(mockUser);
        when(storesRepository.save(any(Stores.class))).thenReturn(mockStore);

        // when
        StoreResponseDto response = storesService.createStore(requestDto, userDetails);

        // then
        assertEquals("가게가 성공적으로 생성되었습니다.", response.getMessage());
        verify(storesRepository, times(1)).save(any(Stores.class));
    }

    @Test
    void 가게_최대생성개수_3개() {
        // given
        StoreCreatedRequestDto requestDto = new StoreCreatedRequestDto(
            "New Store", 
            2, 
            "Category", 
            "Address", 
            "010-1234-5678", 
            "Contents", 
            "picture.jpg", 
            1000, 
            "09:00~22:00", 
            "SUNDAY", 
            5000);

        // 이미 가게 수 3개로 가정
        when(storesService.getStoreCountByUser(any())).thenReturn(3);

        // when & then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> storesService.createStore(requestDto, userDetails));
        assertEquals("가게는 3개 이하만 생성할 수 있습니다.", exception.getMessage());
    }

    @Test
    void 모든_가게_조회() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<Stores> storesList = List.of(
            new Stores("Store 1", 1, "Category", "Address 1", "010-1234-5678", "Content 1", "picture1.jpg", 2000, "09:00~22:00", "SUNDAY", mockUser, 1000),
            new Stores("Store 2", 1, "Category", "Address 2", "010-2345-6789", "Content 2", "picture2.jpg", 3000, "10:00~23:00", "MONDAY", mockUser, 1500)
        );
        Page<Stores> page = new PageImpl<>(storesList);
        when(storesRepository.findAll(pageable)).thenReturn(page);

        // when
        Page<StoresSimpleResponseDto> result = storesService.getStores(1, 10);

        // then
        assertEquals(2, result.getTotalElements());
        verify(storesRepository, times(1)).findAll(pageable);
    }

    @Test
    void 특정_가게_상세_조회() {
        // given
        Long storeId = 1L; // 조회할 가게 ID
        when(storesRepository.findById(storeId)).thenReturn(Optional.of(mockStore)); // 가게를 찾는 로직 모킹

        // when
        StoreDetailResponseDto result = storesService.getStore(storeId); // 가게 조회 메서드 호출

        // then
        assertEquals(mockStore.getName(), result.getName());
        assertEquals(mockStore.getAddress(), result.getAddress());

        verify(storesRepository, times(1)).findById(storeId); // findById 호출 검증
    }

    @Test
    void 특정_가게_상세_조회_존재하지않음() {
        // given
        Long storeId = 1L; // 조회할 가게 ID
        when(storesRepository.findById(storeId)).thenReturn(Optional.empty()); // 가게가 없는 경우 모킹

        // when & then
        Exception exception = assertThrows(InvalidRequestStateException.class, () -> storesService.getStore(storeId));
        assertEquals("Store not found", exception.getMessage());
    }

    @Test
    void 가게_수정_성공() {
        // given
        long storeId = 1L;
        StoreUpdateRequestDto updateRequest = new StoreUpdateRequestDto(
            "Updated Store",
            1,
            "Category",
            "Address",
            "",
            "01012345678",
            "Contents",
            2000,
            3000,
            "10:00~22:00",
            "SATURDAY"
        );

        when(storesRepository.findById(storeId)).thenReturn(Optional.of(mockStore));

        // when
        StoreResponseDto response = storesService.updateStore(storeId, updateRequest);

        // then
        assertEquals("Store updated sucessfully", response.getMessage());
        verify(storesRepository, times(1)).findById(storeId);
        verify(storesRepository, times(1)).save(any(Stores.class));
    }

    @Test
    void 가게_삭제_성공() {
        // given
        long storeId = 1L;
        when(storesRepository.findById(storeId)).thenReturn(Optional.of(mockStore));

        // when
        StoreResponseDto response = storesService.deleteStore(storeId);

        // then
        assertEquals("Store deleted sucessfully", response.getMessage());
        verify(storesRepository, times(1)).findById(storeId);
        verify(storesRepository, times(1)).save(any(Stores.class));
    }

    @Test
    void 가게_삭제_존재하지않음() {
        // given
        long storeId = 1L;
        when(storesRepository.findById(storeId)).thenReturn(Optional.empty());

        // when & then
        Exception exception = assertThrows(InvalidRequestStateException.class, () -> storesService.deleteStore(storeId));
        assertEquals("Store not found", exception.getMessage());
    }

    @Test
    void 가게_검색() {
        // given
        String keyword = "Store";
        Pageable pageable = PageRequest.of(0, 10);
        List<Stores> storesList = List.of(mockStore);
        Page<Stores> page = new PageImpl<>(storesList);
        when(storesRepository.findByNameContaining(keyword, pageable)).thenReturn(page);

        // when
        Page<StoresSimpleResponseDto> result = storesService.searchStores(keyword, 1, 10);

        // then
        assertEquals(1, result.getTotalElements());
        verify(storesRepository, times(1)).findByNameContaining(keyword, pageable);
    }

}
