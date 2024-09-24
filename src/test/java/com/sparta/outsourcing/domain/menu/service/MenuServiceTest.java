package com.sparta.outsourcing.domain.menu.service;

import com.sparta.outsourcing.domain.menu.dto.request.MenuCreateRequestDto;
import com.sparta.outsourcing.domain.menu.dto.request.MenuUpdateRequestDto;
import com.sparta.outsourcing.domain.menu.dto.response.MenuResponseDto;
import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.menu.repository.MenuRepository;
import com.sparta.outsourcing.domain.menu.service.MenuService;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.stores.repository.StoresRepository;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoresRepository storesRepository;

    private User mockUser;
    private Stores mockStore;
    private Menu mockMenu;
    private CustomUserDetails userDetails;

    @BeforeEach //각각의 테스트 코드가 실행되기 전에 수행
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
                "휴일", mockUser,
                10000);

        ReflectionTestUtils.setField(mockStore,
                "id",
                1L);

        userDetails = mock(CustomUserDetails.class);
        when(userDetails.getEmail()).thenReturn("email@email.com");
        when(userDetails.getRole()).thenReturn(Role.ROLE_OWNER);

        mockMenu = new Menu(
                "이미지 주소",
                "이름",
                "설명",
                15000,
                mockStore
        );
        ReflectionTestUtils.setField(mockMenu,
                "id",
                1L
        );

    }

    @Test
    @DisplayName("createMenu 정상 등록")
    void createMenu() {

        // given

        MenuCreateRequestDto menuCreateRequestDto = new MenuCreateRequestDto(
                "이미지 주소",
                "이름",
                "설명",
                15000
        );

        when(storesRepository.findById(mockStore.getId())).thenReturn(Optional.of(mockStore));

        Menu mockMenu = new Menu(
                menuCreateRequestDto.getMenuPictureUrl(),
                menuCreateRequestDto.getName(),
                menuCreateRequestDto.getDescription(),
                menuCreateRequestDto.getPrice(),
                mockStore
        );

        when(storesRepository.findById(anyLong())).thenReturn(Optional.of(mockStore));
        when(menuRepository.save(any())).thenReturn(mockMenu);

        // when
        MenuResponseDto responseDto = menuService.createMenu(mockStore.getId(), menuCreateRequestDto, userDetails);

        // then
        assertNotNull(responseDto);
        assertEquals(mockMenu.getName(), responseDto.getName());
        assertEquals(mockMenu.getDescription(), responseDto.getDescription());
        assertEquals(mockMenu.getPrice(), responseDto.getPrice());
        assertEquals(mockMenu.getMenuPictureUrl(), responseDto.getMenuPictureUrl());

    }


    @Test
    @DisplayName("getMenus 정상 작동")
    void getMenus() {
        // given
        List<Menu> menuList = new ArrayList<>();
        menuList.add(mockMenu);
        when(menuRepository.findByStoreId(mockStore.getId())).thenReturn(menuList);

        // when
        List<MenuResponseDto> responseDtoList = menuService.getMenus(mockStore.getId());

        // then
        assertNotNull(responseDtoList);
        assertEquals(mockMenu.getName(), responseDtoList.get(0).getName());

    }


    @Test
    @DisplayName("updateMenu 정상 작동")
    void updateMenu() {

        Long menuId = 1L;
        MenuUpdateRequestDto menuUpdateRequestDto = new MenuUpdateRequestDto(
                "수정 이미지 주소",
                "수정 이름",
                "수정 설명",
                20000
        );

        when(storesRepository.findById(mockStore.getId())).thenReturn(Optional.of(mockStore));
        when(menuRepository.findMenuById(menuId)).thenReturn(Optional.of(mockMenu));

        MenuResponseDto responseDto = menuService.updateMenu(mockStore.getId(),
                menuId, menuUpdateRequestDto, userDetails);

        assertNotNull(responseDto);
        assertEquals(mockMenu.getName(), responseDto.getName());
    }


    @Test
    @DisplayName("deleteMenu 정상 작동")
    void deleteMenu() {

        Long menuId = 1L;
        when(storesRepository.findById(mockStore.getId())).thenReturn(Optional.of(mockStore));
        when(menuRepository.findMenuById(menuId)).thenReturn(Optional.of(mockMenu));

        menuService.deleteMenu(mockStore.getId(), menuId, userDetails);

        assertTrue(mockMenu.getDeleted());

    }


}
