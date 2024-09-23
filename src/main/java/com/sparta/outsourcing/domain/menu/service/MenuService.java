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

        // 가게 존재 확인
        Stores store = findStoreById(storeId);

//        // 유저가 OWNER 인지 확인
//        if (!userDetails.getRole().equals(ROLE_OWNER)) {
//            throw new IllegalArgumentException("오너 계정만 메뉴를 생성할 수 있습니다.");
//        } // @PreAuthorize("hasAuthority('ROLE_OWNER')") 로 이미 검증 한 상태?

//        // 사용자가 가게의 주인인지 확인
//        if(!store.getUser().getId().equals(userDetails.getEmail())){
//            throw new IllegalArgumentException("store user not match");
//        }

        // 사용자가 가게의 주인인지 확인
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

        // 가게 존재 확인
        Stores store = findStoreById(storeId);

        storeUserMatch(store, userDetails);

        // 메뉴 존재 확인
        Menu menu = findMenuById(menuId);

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
                           MenuDeleteRequestDto menuDeleteRequest,
                           CustomUserDetails userDetails) {

        // 가게 존재 확인
        Stores store = findStoreById(storeId);

        storeUserMatch(store, userDetails);

        // 메뉴 존재 확인
        Menu menu = findMenuById(menuId);

        menu.deleteMenu();

    }


    // Id 로 메뉴 찾고 존재 확인
    public Menu findMenuById(Long menuId) {
        Menu menu = menuRepository.findByMenuId(menuId)
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
        if(!store.getUser().getId().equals(userDetails.getEmail())){
            throw new IllegalArgumentException("해당 가게의 주인이 아닙니다");
        }
    }

}
