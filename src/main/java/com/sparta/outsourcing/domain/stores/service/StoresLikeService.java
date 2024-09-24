package com.sparta.outsourcing.domain.stores.service;

import com.sparta.outsourcing.domain.stores.dto.StoreResponseDto;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.stores.entity.StoresLike;
import com.sparta.outsourcing.domain.stores.repository.StoresLikeRepository;
import com.sparta.outsourcing.domain.stores.repository.StoresRepository;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.Status;
import com.sparta.outsourcing.domain.user.entity.User;
import com.sparta.outsourcing.domain.user.repository.UserRepository;
import com.sparta.outsourcing.exception.ForbiddenException;
import com.sparta.outsourcing.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.outsourcing.domain.user.entity.Role.ROLE_USER;

@Service
@RequiredArgsConstructor
public class StoresLikeService {
    private final StoresRepository storesRepository;
    private final StoresLikeRepository storesLikeRepository;
    private final UserRepository userRepository;

    public StoreResponseDto StoreLike(Long storeId, CustomUserDetails userDetails) {
        // 사용자 역할 확인
        if (!userDetails.getRole().equals(ROLE_USER)) {
            throw new ForbiddenException("사용자 계정으로만 좋아요가 가능합니다.");
        }

        Stores store = storesRepository.findById(storeId)
            .orElseThrow(() -> new NotFoundException("Store not found"));

        // 사용자 조회
        User user = userRepository.findByEmail(userDetails.getEmail())
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Optional<StoresLike> existingLike = storesLikeRepository.findByUserAndStores(user, store);

        if (existingLike.isPresent()) {
            // 이미 좋아요를 누른 상태 -> 좋아요 취소
            storesLikeRepository.delete(existingLike.get());
            return new StoreResponseDto(
                "Store unliked successfully",
                user.getName(),
                200
            ) ;
        } else {
            // 좋아요가 없으면 추가
            StoresLike storesLike = new StoresLike(user, store);
            storesLikeRepository.save(storesLike);
            return new StoreResponseDto(
                "Store liked successfully",
                user.getName(),
                200
            ) ;
        }
    }
}
