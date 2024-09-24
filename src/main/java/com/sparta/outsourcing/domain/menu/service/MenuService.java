package com.sparta.outsourcing.domain.menu.service;

import com.sparta.outsourcing.domain.menu.dto.request.MenuDeleteRequestDto;
import com.sparta.outsourcing.domain.menu.dto.request.MenuCreateRequestDto;
import com.sparta.outsourcing.domain.menu.dto.request.MenuUpdateRequestDto;
import com.sparta.outsourcing.domain.menu.dto.response.MenuResponseDto;
import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.menu.repository.MenuRepository;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.stores.repository.StoresRepository;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoresRepository storesRepository;

    // 메뉴 생성
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public MenuResponseDto createMenu(Long storeId,
                                      MenuCreateRequestDto menuCreateRequest ,
                                      CustomUserDetails userDetails) {

        Stores store = findStoreById(storeId);

        storeUserMatch(store, userDetails);

        Menu menu = new Menu(
                menuCreateRequest.getMenuPictureUrl(),
                menuCreateRequest.getName(),
                menuCreateRequest.getDescription(),
                menuCreateRequest.getPrice(),
                store
        );

        Menu savedMenu = menuRepository.save(menu);

        return new MenuResponseDto(
                savedMenu.getMenuPictureUrl(),
                savedMenu.getName(),
                savedMenu.getDescription(),
                savedMenu.getPrice()
        );

    }

    // 메뉴 조회
    public List<MenuResponseDto> getMenus(Long storeId) {

        // 가게 id가 같은 메뉴 조회
        List<Menu> menuList = menuRepository.findByStoreId(storeId);

        List<MenuResponseDto> menuResponseList = new ArrayList<>();

        for (Menu menu : menuList) {

            MenuResponseDto menuResponse = new MenuResponseDto(
                    menu.getMenuPictureUrl(),
                    menu.getName(),
                    menu.getDescription(),
                    menu.getPrice()
            );
            menuResponseList.add(menuResponse);
        }

        return menuResponseList;

    }

    // 메뉴 수정
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public MenuResponseDto updateMenu(Long storeId, Long menuId,
                                      MenuUpdateRequestDto menuUpdateRequest,
                                      CustomUserDetails userDetails) {

        Stores store = findStoreById(storeId);

        storeUserMatch(store, userDetails);

        Menu menu = findMenuById(menuId);

        storeMenuMatch(storeId,menuId);

        menu.updateMenu(menuUpdateRequest.getMenuPictureUrl(), menuUpdateRequest.getName(), menuUpdateRequest.getDescription(), menuUpdateRequest.getPrice());

        return new MenuResponseDto(
                menu.getMenuPictureUrl(),
                menu.getName(),
                menu.getDescription(),
                menu.getPrice()
        );

    }

    // 메뉴 삭제
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public void deleteMenu(Long storeId, Long menuId,
                           CustomUserDetails userDetails) {

        Stores store = findStoreById(storeId);

        storeUserMatch(store, userDetails);

        Menu menu = findMenuById(menuId);

        storeMenuMatch(storeId,menuId);

        menu.deleteMenu();

    }


    // Id 로 메뉴 찾고 존재 확인
    public Menu findMenuById(Long menuId) {
        Menu menu = menuRepository.findMenuById(menuId)
                .orElseThrow(() -> new NullPointerException("해당 메뉴를 찾을 수 없습니다."));

        return menu;
    }

    // Id로 가게 찾고 존재 확인
    public Stores findStoreById(Long storeId) {
        Stores store = storesRepository.findById(storeId)
                .orElseThrow(() -> new NullPointerException("해당 가게를 찾을 수 없습니다."));

        return store;
    }

    // 사용자가 가게의 주인인지 확인
    public void storeUserMatch(Stores store, CustomUserDetails userDetails) {
        if(!store.getUser().getEmail().equals(userDetails.getEmail())){
            throw new IllegalArgumentException("해당 가게의 주인이 아닙니다");
        }
    }

    // 가게의 메뉴가 맞는지 확인
    public void storeMenuMatch(Long storeId, Long menuId) {
        Menu menu = findMenuById(menuId);

        if (!storeId.equals(menu.getStore().getId())){
            throw new IllegalArgumentException("해당 가게의 메뉴가 아닙니다");
        }
    }

}
