package com.sparta.outsourcing.domain.search.service;

import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.menu.repository.MenuRepository;
import com.sparta.outsourcing.domain.search.dto.SearchRequestDto;
import com.sparta.outsourcing.domain.search.dto.SearchResponseDto;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.stores.repository.StoresRepository;
import com.sparta.outsourcing.exception.BadRequestException;
import com.sparta.outsourcing.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoresRepository storesRepository;

    @InjectMocks
    private SearchService searchService;

    private SearchRequestDto searchRequestDto;

    @BeforeEach
    public void setUp() {
        searchRequestDto = new SearchRequestDto("피자");
    }

    @Test
    public void testSearchByKeyword_ShouldReturnMenusAndStores() {
        // Given
        Stores pizzaStore = new Stores("피자 가게", 1, "", "", "", "", "", 0, "", "", null, 1);
        Menu menu1 = new Menu("", "페퍼로니 피자", "", 1, pizzaStore);
        Menu menu2 = new Menu("", "치즈 피자", "", 1, pizzaStore);

        Page<Menu> menuPage = new PageImpl<>(Collections.singletonList(menu1));
        Page<Stores> storePage = new PageImpl<>(Collections.singletonList(pizzaStore));

        when(menuRepository.findByNameContaining(anyString(), any(Pageable.class))).thenReturn(menuPage);
        when(storesRepository.findByNameContaining(anyString(), any(Pageable.class))).thenReturn(storePage);

        Pageable pageable = Pageable.ofSize(10);

        // When
        SearchResponseDto response = searchService.searchByKeyword(searchRequestDto, pageable);

        // Then
        assertThat(response.getMenuList()).hasSize(1);
        assertThat(response.getStoreList()).hasSize(1);
        assertThat(response.getMenuList().get(0).getName()).isEqualTo("페퍼로니 피자");
        assertThat(response.getStoreList().get(0).getName()).isEqualTo("피자 가게");
    }

    @Test
    public void testSearchByKeyword_EmptyKeyword_ShouldThrowBadRequestException() {
        // Given
        searchRequestDto = new SearchRequestDto("");

        // When & Then
        assertThatThrownBy(() -> searchService.searchByKeyword(searchRequestDto, Pageable.ofSize(10)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Search keyword is required");
    }

    @Test
    public void testSearchByKeyword_NoResults_ShouldThrowNotFoundException() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        when(menuRepository.findByNameContaining(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(storesRepository.findByNameContaining(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        // When & Then
        assertThatThrownBy(() -> searchService.searchByKeyword(searchRequestDto, pageable))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No menu or stores found");
    }
}