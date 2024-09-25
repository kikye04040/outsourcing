package com.sparta.outsourcing.domain.menu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 테스트 코드 작성중 확인 필요
public class MenuCreateRequestDto {

    private String menuPictureUrl;
    private String name;
    private String description;
    private Integer price;

}
