package com.sparta.outsourcing.domain.stores.service;

import com.sparta.outsourcing.domain.stores.dto.*;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.stores.enums.StoreStatus;
import com.sparta.outsourcing.domain.stores.repository.StoresRepository;
import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.User;
import com.sparta.outsourcing.domain.user.repository.UserRepository;
import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.outsourcing.domain.user.entity.Role.ROLE_OWNER;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoresService {
    private final StoresRepository storesRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('OWNER')")
    public StoreResponseDto createStore(StoreCreatedRequestDto req, CustomUserDetails userDetails) {
        // 유저 상태 확인(NORMAL)
        User user = userRepository.findByEmailOrElseThrow(userDetails.getEmail());

        // 유저가 OWNER 인지 확인
        if (!userDetails.getRole().equals(ROLE_OWNER)) {
            throw new IllegalArgumentException("오너 계정만 가게를 생성할 수 있습니다.");
        }

        // 유저가 최대 가게 개수(3개)를 초과했는지 확인
        if(storesRepository.countByUserId(user.getId()) >= 3){
            throw new IllegalArgumentException("가게 생성은 최대 3개까지만 가능합니다.");
        };


        Stores newStores = new Stores(
            req.getName(),
            req.getType(),
            req.getCategory(),
            req.getAddress(),
            req.getPhone(),
            req.getContents(),
            req.getStorePictureUrl(),
            req.getDeliveryTip(),
            req.getOperationHours(),
            req.getClosedDays(),
            user
        );

        Stores savedStores = storesRepository.save(newStores);

        return new StoreResponseDto(
            "Store created successfully",
            savedStores.getName(),
            200
        );

    }

    public Page<StoresSimpleResponseDto> getStores(int page, int size){
        Pageable pageable = PageRequest.of(page-1 , size);

        Page<Stores> stores = storesRepository.findAll(pageable);

        return stores.map(req -> new StoresSimpleResponseDto(
            req.getName(),
            req.getStorePictureUrl(),
            req.getDibsCount(),
            req.getReviewCount(),
            req.getDeliveryTip(),
            req.getMinDeliveryTime(),
            req.getMaxDeliveryTime()
        ));
    }


    public StoreDetailResponseDto getStore(Long storeId) {
        Stores req = storesRepository.findById(storeId)
            .orElseThrow(() -> new InvalidRequestStateException("Store not found"));

        return new StoreDetailResponseDto(
            req.getName(),
            req.getType(),
            req.getCategory(),
            req.getAddress(),
            req.getStorePictureUrl(),
            req.getPhone(),
            req.getContents(),
            req.getMinDeliveryPrice(),
            req.getDeliveryTip(),
            req.getMinDeliveryTime(),
            req.getMaxDeliveryTime(),
            req.getDibsCount(),
            req.getReviewCount(),
            req.getOperationHours(),
            req.getClosedDays()
            // 메뉴 목록 추가
        );
    }

    @Transactional
    public StoreResponseDto updateStore(long storeId, StoreUpdateRequestDto req) {
        Stores stores = storesRepository.findById(storeId)
            .orElseThrow(() -> new InvalidRequestStateException("Store not found"));

        stores.update(
            req.getName(),
            req.getAddress(),
            req.getStorePictureUrl(),
            req.getPhone(),
            req.getContents(),
            req.getMinDeliveryPrice(),
            req.getDeliveryTip(),
            req.getOperationHours(),
            req.getClosedDays()
        );

        return new StoreResponseDto(
            "Store updated sucessfully",
            req.getName(),
            200
        );
    }

    @Transactional
    public StoreResponseDto deleteStore(long storeId) {
        // 유저 유효성 검사

        Stores stores = storesRepository.findById(storeId)
            .orElseThrow(() -> new InvalidRequestStateException("Store not found"));

        // 가게 담당자 일치 여부 확인

        stores.setStoreStatus(StoreStatus.Deleted);

        return new StoreResponseDto(
            "Store updated sucessfully",
            "",
            200
        );
    }



    public Page<StoresSimpleResponseDto> searchStores(String keyword, int page, int size){
        Pageable pageable = PageRequest.of(page-1 , size);

        Page<Stores> storesList = storesRepository.findByNameContaining(keyword, pageable);
        log.info("Service Keyword: "+ keyword);
        return storesList.map(req -> new StoresSimpleResponseDto(
            req.getName(),
            req.getStorePictureUrl(),
            req.getDibsCount(),
            req.getReviewCount(),
            req.getDeliveryTip(),
            req.getMinDeliveryTime(),
            req.getMaxDeliveryTime()
        ));
    }

}
