package com.sparta.outsourcing.domain.menu.controller;

import com.sparta.outsourcing.domain.menu.dto.request.MenuCreateRequestDto;
import com.sparta.outsourcing.domain.menu.dto.request.MenuUpdateRequestDto;
import com.sparta.outsourcing.domain.menu.dto.response.MenuResponseDto;
import com.sparta.outsourcing.domain.menu.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // store 에 메뉴 생성
    @PostMapping("/stores/{storeId}/menus")
    public MenuResponseDto createMenu(@PathVariable Long storeId, @RequestBody MenuCreateRequestDto menuSaveRequest) {
        return menuService.createMenu(storeId, menuSaveRequest);
    }

    // store의 메뉴 조회
    @GetMapping("/stores/{storeId}/menus")
    public List<MenuResponseDto> getMenus(@PathVariable Long storeId) {
        return menuService.getMenus(storeId);
    }

    // store 의 menu 수정
    @PutMapping("/stores/menus/{menuId}")
    public MenuResponseDto updateManu(@PathVariable Long storeId, Long menuId, @RequestBody MenuUpdateRequestDto menuUpdateRequest) {
        return menuService.updateMenu(menuId, menuUpdateRequest);
    }

//    @PutMapping("/stores/menus/{menuId}")
//    public void deleteMenu(@PathVariable Long menuId @RequestBody MenuDeleteRequest menuDeleterequest) {
//        menuService.deleteMenu(menuId, menuDeleterequest);
//    }

}
