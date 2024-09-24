package com.sparta.outsourcing.domain.search.controller;

import com.sparta.outsourcing.domain.search.service.SearchService;
import com.sparta.outsourcing.domain.search.dto.SearchRequestDto;
import com.sparta.outsourcing.domain.search.dto.SearchResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public SearchResponseDto search(@RequestBody SearchRequestDto searchRequestDto,
                                    @RequestParam Pageable pageable) {

        return searchService.searchByKeyword(searchRequestDto, pageable);
    }
}
