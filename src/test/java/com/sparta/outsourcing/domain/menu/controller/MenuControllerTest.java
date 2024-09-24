package com.sparta.outsourcing.domain.menu.controller;

import com.sparta.outsourcing.domain.menu.dto.request.MenuCreateRequestDto;
import com.sparta.outsourcing.domain.menu.dto.response.MenuResponseDto;
import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.menu.service.MenuService;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class MenuControllerTest {

    @Mock
    private MenuService menuService;

    @InjectMocks
    private MenuController menuController;

    private User mockUser;
    private Stores mockStore;
    private Menu mockMenu;
    private CustomUserDetails userDetails;

    @BeforeEach
        //각각의 테스트 코드가 실행되기 전에 수행
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = User.builder()
                .email("email@email.com")
                .name("name1")
                .build();

        mockStore = new Stores(
                "가게 이름",
                1,
                "종류",
                "주소",
                "010-1234-5678",
                "소개?",
                "이미지 url",
                3000,
                "영업 시간",
                "휴일",
                mockUser,
                10000);

        ReflectionTestUtils.setField(mockStore,
                "id",
                1L);

        userDetails = mock(CustomUserDetails.class);
        when(userDetails.getEmail()).thenReturn("email@email.com");
        when(userDetails.getRole()).thenReturn(Role.ROLE_OWNER);

    }


    @Test
    @DisplayName("메뉴 생성 성공")
    void createMenu() {

        Long storeId = 1L;

        MenuCreateRequestDto menuCreateRequestDto = new MenuCreateRequestDto(
                "이미지 url",
                "이름",
                "설명",
                12000
        );
        MenuResponseDto menuResponseDto = new MenuResponseDto(
                menuCreateRequestDto.getMenuPictureUrl(),
                menuCreateRequestDto.getName(),
                menuCreateRequestDto.getDescription(),
                menuCreateRequestDto.getPrice()
        );

        when(menuService.createMenu(anyLong(), any(MenuCreateRequestDto.class), any(CustomUserDetails.class)))
                .thenReturn(menuResponseDto);

        ResponseEntity<MenuResponseDto> response = menuController.createMenu(
                storeId,menuCreateRequestDto,userDetails);

        assertNotNull(response.getBody());
        assertEquals(menuResponseDto, response.getBody());
        verify(menuService, times(1)).createMenu(
                storeId, menuCreateRequestDto, userDetails);

    }


    @Test
    @DisplayName("메뉴 조회 성공")
    void getMenu() {

        List<MenuResponseDto> menuResponseDtoList = new ArrayList<>();


    }


    @Test
    @DisplayName("메뉴 수정 성공")
    void updateMenu() {
        Long storeId = 1L;



    }


    @Test
    @DisplayName("메뉴 삭제 성공")
    void deleteMenu() {
        Long storeId = 1L;



    }


}
