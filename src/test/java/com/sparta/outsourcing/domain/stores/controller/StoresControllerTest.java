package com.sparta.outsourcing.domain.stores.controller;

import com.sparta.outsourcing.domain.stores.dto.*;
import com.sparta.outsourcing.domain.stores.service.StoresService;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class StoresControllerTest {

    @Mock
    private StoresService storesService;

    @InjectMocks
    private StoresController storesController;

    private CustomUserDetails userDetails;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetails = CustomUserDetails.builder()
            .email("test@user.com")
            .name("Test User")
            .role(com.sparta.outsourcing.domain.user.entity.Role.ROLE_OWNER) // 가게 생성을 위한 ROLE_OWNER
            .build();
    }



    @Test
    void 전체_가게_조회_성공() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<StoresSimpleResponseDto> storeList = List.of(
            new StoresSimpleResponseDto("황비홍 마라탕", "", 3000),
            new StoresSimpleResponseDto("파스타", "Store 2", 2000)
        );
        Page<StoresSimpleResponseDto> page = new PageImpl<>(storeList);

        when(storesService.getStores(anyInt(), anyInt())).thenReturn(page);

        // when
        ResponseEntity<Page<StoresSimpleResponseDto>> response = storesController.getStores(1, 10);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getTotalElements());
        verify(storesService, times(1)).getStores(anyInt(), anyInt());
    }

    @Test
    void 특정_가게_상세_조회_Success() {
        // given
        Long storeId = 1L;
        StoreDetailResponseDto storeDetailResponseDto = new StoreDetailResponseDto(
            "파스타", 1, "Category", "Address", "", "01012345678", "안녕하세요.", 1000,
            3000, "9to9", "SUNDAY");

        when(storesService.getStore(storeId)).thenReturn(storeDetailResponseDto);

        // when
        ResponseEntity<StoreDetailResponseDto> response = storesController.getStore(storeId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("파스타", response.getBody().getName());
        verify(storesService, times(1)).getStore(storeId);
    }

    @Test
    void 가게_검색_Success() {
        // given
        String keyword = "마라";
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<StoresSimpleResponseDto> storeList = List.of(
            new StoresSimpleResponseDto("마라탕", "Test Store 1", 500),
            new StoresSimpleResponseDto("파스타", "Test Store 2", 1000)
        );
        Page<StoresSimpleResponseDto> page = new PageImpl<>(storeList);

        when(storesService.searchStores(keyword, 1, 10)).thenReturn(page);

        // when
        ResponseEntity<Page<StoresSimpleResponseDto>> response = storesController.searchStores(keyword, 1, 10);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getTotalElements());
        verify(storesService, times(1)).searchStores(keyword, 1, 10);
    }

    @Test
    void 가게_삭제_Success() {
        // given
        long storeId = 1L;
        StoreResponseDto responseDto = new StoreResponseDto(
            "Store deleted sucessfully",
            "",
            200);

        when(storesService.deleteStore(storeId)).thenReturn(responseDto);

        // when
        StoreResponseDto response = storesController.deleteStore(storeId);

        // then
        assertEquals("Store deleted sucessfully", response.getMessage());
        verify(storesService, times(1)).deleteStore(storeId);
    }

    @Test
    void 가게_생성_Success() {
        // given
        StoreCreatedRequestDto requestDto = new StoreCreatedRequestDto(
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
            5000);

        StoreResponseDto responseDto = new StoreResponseDto(
            "가게가 성공적으로 생성되었습니다.", "Test Store", 200);

        when(storesService.createStore(any(StoreCreatedRequestDto.class), any(CustomUserDetails.class)))
            .thenReturn(responseDto);

        // when
        ResponseEntity<StoreResponseDto> response = storesController.createStore(requestDto, userDetails);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Store", response.getBody().getName());
        verify(storesService, times(1)).createStore(any(StoreCreatedRequestDto.class), any(CustomUserDetails.class));
    }

}
