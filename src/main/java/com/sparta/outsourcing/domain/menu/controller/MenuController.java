package com.sparta.outsourcing.domain.menu.controller;

import com.sparta.outsourcing.domain.menu.dto.request.MenuDeleteRequest;
import com.sparta.outsourcing.domain.menu.dto.request.MenuSaveRequest;
import com.sparta.outsourcing.domain.menu.dto.request.MenuUpdateRequest;
import com.sparta.outsourcing.domain.menu.dto.response.MenuResponse;
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
    public MenuResponse createMenu(@PathVariable Long storeId, @RequestBody MenuSaveRequest menuSaveRequest) {
        return menuService.createMenu(storeId, menuSaveRequest);
    }

    // store의 메뉴 조회
    @GetMapping("/stores/{storeId}/menus")
    public List<MenuResponse> getMenus(@PathVariable Long storeId) {
        return menuService.getMenus(storeId);
    }

    // store 의 menu 수정
    @PutMapping("/stores/menus/{menuId}")
    public MenuResponse updateManu(@PathVariable Long storeId, Long menuId, @RequestBody MenuUpdateRequest menuUpdateRequest) {
        return menuService.updateMenu(menuId, menuUpdateRequest);
    }

//    @PutMapping("/stores/menus/{menuId}")
//    public void deleteMenu(@PathVariable Long menuId @RequestBody MenuDeleteRequest menuDeleterequest) {
//        menuService.deleteMenu(menuId, menuDeleterequest);
//    }

}
