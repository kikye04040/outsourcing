package com.sparta.outsourcing.domain.menu.controller;

import com.sparta.outsourcing.domain.menu.dto.request.MenuCreateRequestDto;
import com.sparta.outsourcing.domain.menu.dto.request.MenuDeleteRequestDto;
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

    // 메뉴 생성
    @PostMapping("/stores/{storeId}/menus")
    public MenuResponseDto createMenu(@PathVariable Long storeId, @RequestBody MenuCreateRequestDto menuSaveRequestDto) {
        return menuService.createMenu(storeId, menuSaveRequestDto);
    }

    // 메뉴 조회
    @GetMapping("/stores/{storeId}/menus")
    public List<MenuResponseDto> getMenus(@PathVariable Long storeId) {
        return menuService.getMenus(storeId);
    }

    // 메뉴 수정
    @PutMapping("/stores/menus/{menuId}")
    public MenuResponseDto updateManu(@PathVariable Long menuId, @RequestBody MenuUpdateRequestDto menuUpdateRequestDto) {
        return menuService.updateMenu(menuId, menuUpdateRequestDto);
    }

    // 메뉴 삭제 상태로 변경
    @PutMapping("/stores/menus/{menuId}/delete")
    public void deleteMenu(@PathVariable Long menuId, @RequestBody MenuDeleteRequestDto menuDeleterequestDto) {
        menuService.deleteMenu(menuId, menuDeleterequestDto);
    }

}
