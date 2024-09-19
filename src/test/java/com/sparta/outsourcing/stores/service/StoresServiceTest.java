package com.sparta.outsourcing.stores.service;

import com.sparta.outsourcing.domain.stores.dto.StoreCreatedRequestDto;
import com.sparta.outsourcing.domain.stores.dto.StoreResponseDto;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.stores.repository.StoresRepository;
import com.sparta.outsourcing.domain.stores.service.StoresService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoresServiceTest {

    @InjectMocks
    private StoresService storesService;

    @Mock
    private StoresRepository storesRepository;

    @Test
    void createStore() {
        // given
        StoreCreatedRequestDto req = new StoreCreatedRequestDto(
            "황비홍 마라탕",
            1,
            "중식",
            "서울시 관악구 신림동",
            "070-1234-1234",
            "안녕하세요. 늘 깨끗하고 맛있는 음식으로 보답하겠습니다.",
            "",
            500,
            "평일 오전 10:00 ~ 오후 09:00",
            "매주 일요일 휴무"
        );
        Stores stores = new Stores(
            req.getName(),
            req.getType(),
            req.getCategory(),
            req.getAddress(),
            req.getPhone(),
            req.getContents(),
            req.getStorePictureUrl(),
            req.getDeliveryTip(),
            req.getOperationHours(),
            req.getClosedDays()
        );

        when(storesRepository.save(any(Stores.class))).thenReturn(stores);

        // when
        StoreResponseDto res = storesService.createStore(req);

        // then
        assertNotNull(res);
        assertEquals(stores.getName(), res.getName());
    }

    @Test
    void getStores() {
    }

    @Test
    void getStore() {
    }

    @Test
    void updateStore() {
    }

    @Test
    void deleteStore() {
    }
}