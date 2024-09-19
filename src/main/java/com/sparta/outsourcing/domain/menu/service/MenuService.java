package com.sparta.outsourcing.domain.menu.service;

import com.sparta.outsourcing.domain.menu.dto.request.MenuSaveRequest;
import com.sparta.outsourcing.domain.menu.dto.request.MenuUpdateRequest;
import com.sparta.outsourcing.domain.menu.dto.response.MenuResponse;
import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;


    // 메뉴 생성
    public MenuResponse saveMenu(MenuSaveRequest menuSaveRequest) {

        // 존재하는 가게인지 확인하는 부분 추가할 것

        // 사용자가 가게의 주인인지 확인하는 부분 추가할 것

        Menu menu = new Menu(
                menuSaveRequest.getName(),
                menuSaveRequest.getDescription(),
                menuSaveRequest.getPrice()
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

        // 존재하는 가게인지 확인하는 부분 추가할 것

        // 이후 가게 Id 받아오는 것으로 고칠 것
        List<Menu> menuList = menuRepository.findAll();

        List<MenuResponse> menuResponseList = new ArrayList<>();

        for (Menu menu : menuList) {

            // deleted 가 true 일 경우 조회되지 않도록 변경할 것
            String menuName = menu.getDeleted() ? null : menu.getName();

            MenuResponse menuResponse = new MenuResponse(
                    menu.getName(),
                    menu.getDescription(),
                    menu.getPrice()
            );
            menuResponseList.add(menuResponse);
        }

        return menuResponseList;

    }

//    // 메뉴 수정
//    @Transactional
//    public MenuResponse updateMenu(Long menuId, MenuUpdateRequest menuUpdateRequest) {
//
//        // 존재하는 가게인지 확인하는 부분 추가할 것
//
//
//    }

    // 메뉴 삭제


}
