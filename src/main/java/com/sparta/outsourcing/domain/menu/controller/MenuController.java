package com.sparta.outsourcing.domain.menu.controller;

import com.sparta.outsourcing.domain.menu.dto.request.MenuSaveRequest;
import com.sparta.outsourcing.domain.menu.dto.response.MenuResponse;
import com.sparta.outsourcing.domain.menu.service.MenuService;
import com.sparta.outsourcing.domain.stores.service.StoresService;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuController {
    private final MenuService menuService;
    private final StoresService storesService;

    @PostMapping
    public ResponseEntity<MenuResponse> saveMenu(@RequestBody MenuSaveRequest menuSaveRequest,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        MenuResponse response = menuService.saveMenu(menuSaveRequest, userDetails);

        return ResponseEntity.ok(response);
    }
}
