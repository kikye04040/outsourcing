package com.sparta.outsourcing.domain.search.service;

import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.menu.repository.MenuRepository;
import com.sparta.outsourcing.domain.search.dto.SearchRequestDto;
import com.sparta.outsourcing.domain.search.dto.SearchResponseDto;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.stores.repository.StoresRepository;
import com.sparta.outsourcing.exception.BadRequestException;
import com.sparta.outsourcing.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MenuRepository menuRepository;
    private final StoresRepository storesRepository;

    public SearchResponseDto searchByKeyword(SearchRequestDto searchRequestDto, Pageable pageable) {
        if(searchRequestDto.getKeyword() == null || searchRequestDto.getKeyword().isEmpty()) {
            throw new BadRequestException("Search keyword is required");
        }

        Page<Menu> menus = menuRepository.findByNameContaining(searchRequestDto.getKeyword(), pageable);
        Page<Stores> stores = storesRepository.findByNameContaining(searchRequestDto.getKeyword(), pageable);

        if(menus.isEmpty() && stores.isEmpty()) {
            throw new NotFoundException("No menu or stores found");
        }

        return new SearchResponseDto(menus, stores);
    }
}
