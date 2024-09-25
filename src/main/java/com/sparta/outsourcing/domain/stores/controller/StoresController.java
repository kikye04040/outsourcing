package com.sparta.outsourcing.domain.stores.controller;

import com.sparta.outsourcing.domain.stores.dto.*;
import com.sparta.outsourcing.domain.stores.service.StoresService;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StoresController {

    private final StoresService storesService;

    @PostMapping("/stores")
    public ResponseEntity<StoreResponseDto> createStore(
            @RequestBody StoreCreatedRequestDto storeCreatedRequestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        StoreResponseDto responseDto = storesService.createStore(storeCreatedRequestDto, userDetails);
        return ResponseEntity.ok(responseDto);
    }

    // 전체 조회
    @GetMapping("/stores")
    public ResponseEntity<Page<StoresSimpleResponseDto>> getStores(@RequestParam(defaultValue = "1") int page,
                                                                   @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(storesService.getStores(page, size));
    }

    // 가게명 검색
    @GetMapping("/stores/search")
    public ResponseEntity<Page<StoresSimpleResponseDto>> searchStores(@RequestParam String keyword, @RequestParam(defaultValue = "1") int page,
                                                                        @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(storesService.searchStores(keyword, page, size));
    }

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<StoreDetailResponseDto> getStore(@PathVariable Long storeId){
        return ResponseEntity.ok(storesService.getStore(storeId));
    }

    @PutMapping("/stores/{storeId}")
    public StoreResponseDto updateStore(@PathVariable long storeId, @RequestBody StoreUpdateRequestDto storeUpdateRequestDto){
        return storesService.updateStore(storeId, storeUpdateRequestDto);
    }

    @DeleteMapping("/stores/{storeId}")
    public StoreResponseDto deleteStore(@PathVariable long storeId){
        return storesService.deleteStore(storeId);
    }
}
