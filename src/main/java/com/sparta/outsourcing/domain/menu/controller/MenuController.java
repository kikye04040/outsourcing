package com.sparta.outsourcing.domain.menu.controller;

import com.sparta.outsourcing.domain.menu.dto.request.MenuCreateRequestDto;
import com.sparta.outsourcing.domain.menu.dto.request.MenuDeleteRequestDto;
import com.sparta.outsourcing.domain.menu.dto.request.MenuUpdateRequestDto;
import com.sparta.outsourcing.domain.menu.dto.response.MenuResponseDto;
import com.sparta.outsourcing.domain.menu.service.MenuService;
import com.sparta.outsourcing.domain.stores.service.StoresService;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final StoresService storesService;

    // 메뉴 생성
    @PostMapping("/stores/{storeId}/menus")
    public ResponseEntity<MenuResponseDto> createMenu(@PathVariable Long storeId,
                                      @RequestBody MenuCreateRequestDto menuSaveRequestDto,
                                      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        MenuResponseDto menuResponseDto = menuService.createMenu(storeId, menuSaveRequestDto, customUserDetails);

        return ResponseEntity.ok(menuResponseDto);
    }

    // 메뉴 조회
    @GetMapping("/stores/{storeId}/menus")
    public ResponseEntity<List<MenuResponseDto>> getMenus(@PathVariable Long storeId) {
        List<MenuResponseDto> menuResponseDtos = menuService.getMenus(storeId);

        return ResponseEntity.ok(menuResponseDtos);
    }

    // 메뉴 수정
    @PutMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long storeId,
                                                      @PathVariable Long menuId, @RequestBody MenuUpdateRequestDto menuUpdateRequestDto) {
        MenuResponseDto menuResponseDto = menuService.updateMenu(storeId, menuId, menuUpdateRequestDto);

        return ResponseEntity.ok(menuResponseDto);
    }

    // 메뉴 삭제 상태로 변경
    @PutMapping("/stores/{storeId}/menus/{menuId}/delete")
    public void deleteMenu(@PathVariable Long storeId, @PathVariable Long menuId, @RequestBody MenuDeleteRequestDto menuDeleterequestDto) {
        menuService.deleteMenu(storeId, menuId, menuDeleterequestDto);
    }
    
    
}    
    

//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/menus")
//public class MenuController {
//    private final MenuService menuService;
//    private final StoresService storesService;
//
//    @PostMapping
//    public ResponseEntity<MenuResponse> saveMenu(@RequestBody MenuSaveRequest menuSaveRequest,
//                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
//        MenuResponse response = menuService.saveMenu(menuSaveRequest, userDetails);
//
//        return ResponseEntity.ok(response);
//    }
//}
