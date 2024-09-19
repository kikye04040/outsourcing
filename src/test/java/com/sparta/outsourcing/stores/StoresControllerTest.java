package com.sparta.outsourcing.stores;

import com.sparta.outsourcing.stores.dto.StoreCreatedRequestDto;
import com.sparta.outsourcing.stores.dto.StoreResponseDto;
import com.sparta.outsourcing.stores.service.StoresService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StoresControllerTest {

    @Mock
    private StoresService storesService;

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


        // when
        StoreResponseDto responseDto = storesService.createStore(req);

        // then
        assertNotNull(responseDto);
    }
}