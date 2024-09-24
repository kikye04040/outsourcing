package com.sparta.outsourcing.domain.search.dto;

import lombok.Getter;

@Getter
public class SearchRequestDto {

    private String keyword;

    public SearchRequestDto(String keyword) {
        this.keyword = keyword;
    }

}
