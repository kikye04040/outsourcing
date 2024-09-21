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
import jakarta.persistence.Convert;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public MenuResponseDto createMenu(Long storeId, MenuCreateRequestDto menuCreateRequest) {

        // 가게 존재 확인
        Stores store = storesRepository.findById(storeId)
                .orElseThrow(() -> new NullPointerException("store not found"));

        // SecurityContext에서 현재 사용자 정보 가져오기
        CustomUserDetails loginUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 사용자가 가게의 주인인지 확인
//        if(!store.getUser.getId().equals(loginUser.getEmail())){
//            throw new IllegalArgumentException("Store User not match");
//        } // store에 아직 얀관관계 설정이 없는 듯?

        Menu menu = new Menu(
                menuCreateRequest.getName(),
                menuCreateRequest.getDescription(),
                menuCreateRequest.getPrice(),
                store
        );

        Menu savedMenu = menuRepository.save(menu);

        return new MenuResponseDto(
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
    public MenuResponseDto updateMenu(Long menuId, MenuUpdateRequestDto menuUpdateRequest) {

        // 가게 존재 확인
        Stores store = storesRepository.findById(menuUpdateRequest.getStores().getId())
                .orElseThrow(() -> new NullPointerException("store not found"));

        // SecurityContext에서 현재 사용자 정보 가져오기
        CustomUserDetails loginUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 사용자가 가게의 주인인지 확인
//        if(!store.getUser.getId().equals(loginUser.getEmail())){
//            throw new IllegalArgumentException("Store User not match");
//        }

        // 메뉴 존재 확인
        Menu menu = findMenuById(menuId);

        menu.updateMenu(menuUpdateRequest.getName(), menuUpdateRequest.getDescription(), menuUpdateRequest.getPrice());

        return new MenuResponseDto(
                menu.getName(),
                menu.getDescription(),
                menu.getPrice()
        );

    }

    // 메뉴 삭제
    @Transactional
    public void deleteMenu(Long menuId, MenuDeleteRequestDto menuDeleteRequest) {

        // 가게 존재 확인
        Stores store = storesRepository.findById(menuDeleteRequest.getStores().getId())
                .orElseThrow(() -> new NullPointerException("store not found"));

        // SecurityContext에서 현재 사용자 정보 가져오기
        CustomUserDetails loginUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 사용자가 가게의 주인인지 확인

//        // 이 경우 사용자 id를 받아와야 함  다른 방법 생각
//        if(!store.getUser.getId().equals(userId)){
//            throw new IllegalArgumentException("Store User not match");
//        }

        // AOP를 통한 검증  코드 중복은 줄어들지만 고려할 부분 있음

        // 토큰에서 받아와서 비교 < - 가장 무난 할 듯
//        if(!store.getUser.getId().equals(loginUser.getEmail())){
//            throw new IllegalArgumentException("Store User not match");
//        }


        // 메뉴 존재 확인
        Menu menu = findMenuById(menuId);

        menu.deleteMenu();

    }


    // Id 로 메뉴 찾고 존재 확인
    public Menu findMenuById(Long menuId) {
        Menu menu = menuRepository.findByMenuId(menuId)
                .orElseThrow(() -> new NullPointerException("Menu not found"));

        return menu;
    }

}
