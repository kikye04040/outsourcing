package com.sparta.outsourcing.domain.stores.controller;

import com.sparta.outsourcing.domain.stores.dto.*;
import com.sparta.outsourcing.domain.stores.service.StoresService;
import com.sparta.outsourcing.stores.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StoresController {

    private final StoresService storesService;

    @PostMapping("/stores")
    public StoreResponseDto createStore(@RequestBody StoreCreatedRequestDto storeCreatedRequestDto){
        return storesService.createStore(storeCreatedRequestDto);
    }

    @GetMapping("/stores")
    public ResponseEntity<Page<StoresSimpleResponseDto>> getStores(@RequestParam(defaultValue = "1") int page,
                                                                   @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(storesService.getStores(page, size));
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
