package com.sparta.outsourcing.domain.menu.service;

import com.sparta.outsourcing.domain.menu.dto.request.MenuDeleteRequest;
import com.sparta.outsourcing.domain.menu.dto.request.MenuSaveRequest;
import com.sparta.outsourcing.domain.menu.dto.request.MenuUpdateRequest;
import com.sparta.outsourcing.domain.menu.dto.response.MenuResponse;
import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.menu.repository.MenuRepository;
import com.sparta.outsourcing.stores.entity.Stores;
import com.sparta.outsourcing.stores.repository.StoresRepository;
import lombok.RequiredArgsConstructor;
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
    public MenuResponse createMenu(Long storeId, MenuSaveRequest menuSaveRequest) {

        Stores store = storesRepository.findById(storeId)
                .orElseThrow(() -> new NullPointerException("store not found"));

        // 사용자가 가게의 주인인지 확인하는 부분 추가할 것

        Menu menu = new Menu(
                menuSaveRequest.getName(),
                menuSaveRequest.getDescription(),
                menuSaveRequest.getPrice(),
                store
        );

        Menu savedMenu = menuRepository.save(menu);

        return new MenuResponse(
                savedMenu.getName(),
                savedMenu.getDescription(),
                savedMenu.getPrice()
        );

    }

    // 메뉴 조회
    public List<MenuResponse> getMenus(Long storeId) {

        // 가게 id가 같은 메뉴
        List<Menu> menuList = menuRepository.findByStoreId(storeId);

        List<MenuResponse> menuResponseList = new ArrayList<>();

        for (Menu menu : menuList) {

            // deleted 가 true 일 경우 조회되지 않도록 하는 부분은 repository 쿼리 부분에
            MenuResponse menuResponse = new MenuResponse(
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
    public MenuResponse updateMenu(Long menuId, MenuUpdateRequest menuUpdateRequest) {

        // 메뉴 존재 확인
        Menu menu = findMenuById(menuId);

        // 사용자가 가게의 주인인지 확인 하는 부분


        menu.updateMenu(menuUpdateRequest.getName(), menuUpdateRequest.getDescription(), menuUpdateRequest.getPrice());

        return new MenuResponse(
                menu.getName(),
                menu.getDescription(),
                menu.getPrice()
        );

    }

    // 메뉴 삭제
    @Transactional
    public void deleteMenu(Long menuId, MenuDeleteRequest menuDeleteRequest) {

        Menu menu = findMenuById(menuId);

        // menuDeleteRequest 로 확인 필요

        // 사용자가 존재하는지 확인하는 부분

        menu.deleteMenu();

    }


    // Id 로 메뉴 찾고 존재 확인
    public Menu findMenuById(Long menuId) {
        Menu menu = menuRepository.findByMenuId(menuId)
                .orElseThrow(() -> new NullPointerException("Menu not found"));

        return menu;
    }

}
