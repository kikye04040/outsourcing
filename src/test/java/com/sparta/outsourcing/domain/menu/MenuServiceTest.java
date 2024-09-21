package com.sparta.outsourcing.domain.menu;

import com.sparta.outsourcing.domain.menu.dto.request.MenuCreateRequestDto;
import com.sparta.outsourcing.domain.menu.dto.response.MenuResponseDto;
import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.menu.repository.MenuRepository;
import com.sparta.outsourcing.domain.menu.service.MenuService;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.stores.repository.StoresRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoresRepository storesRepository;

    @Test
    @DisplayName("createMenu 정상 등록")
    @WithMockUser(username = "name", roles = "OWNER")
    void createMenu() {

        // given
        long storeId = 1;

        MenuCreateRequestDto menuCreateRequestDto = new MenuCreateRequestDto(
                "이름",
                "설명",
                15000
        );

        Stores stores = new Stores(
                "이름",
                1,
                "종류",
                "주소",
                "번호",
                "설명",
                "사진 url",
                1000,
                "영업 시간",
                "휴일"
        );

        Menu menu = new Menu(
                menuCreateRequestDto.getName(),
                menuCreateRequestDto.getDescription(),
                menuCreateRequestDto.getPrice(),
                stores
        );

        given(storesRepository.findById(anyLong())).willReturn(Optional.of(stores));
        given(menuRepository.save(any())).willReturn(menu);

        // when
        MenuResponseDto menuResponseDto = menuService.createMenu(storeId, menuCreateRequestDto);

        // then
        assertNotNull(menuResponseDto);

    }







}
