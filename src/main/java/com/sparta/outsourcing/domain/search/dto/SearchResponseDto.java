package com.sparta.outsourcing.domain.search.dto;

import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class SearchResponseDto {

    private List<Menu> menuList;
    private List<Stores> storeList;
    private int totalPage;
    private long totalContens;

    public SearchResponseDto(Page<Menu> menuList, Page<Stores> storeList) {
        this.menuList = menuList.getContent();
        this.storeList = storeList.getContent();
    }
}
