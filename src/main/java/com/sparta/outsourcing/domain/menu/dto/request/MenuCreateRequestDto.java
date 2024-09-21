package com.sparta.outsourcing.domain.menu.dto.request;

import com.sparta.outsourcing.domain.stores.entity.Stores;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 테스트코드 작성 중 발견 이후 확인 할 것
public class MenuCreateRequestDto {

    private Long storeId;

    private String name;
    private String description;
    private Integer price;

}
