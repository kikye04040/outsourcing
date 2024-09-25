package com.sparta.outsourcing.domain.stores.controller;

import com.sparta.outsourcing.domain.stores.dto.StoreResponseDto;
import com.sparta.outsourcing.domain.stores.service.StoresLikeService;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoresLikeController {

    private final StoresLikeService storesLikeService;

    @PostMapping("/{storeId}/like")
    public ResponseEntity<StoreResponseDto> toggleLike(@PathVariable Long storeId,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        StoreResponseDto responseDto = storesLikeService.StoreLike(storeId, userDetails);
        return ResponseEntity.ok(responseDto);
    }

    // 사용자가 좋아요한 가게 조회
    @GetMapping("/likes")
    public List<StoreResponseDto> getLikedStores(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return storesLikeService.getLikedStores(userDetails);
    }
}
