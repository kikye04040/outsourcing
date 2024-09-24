package com.sparta.outsourcing.domain.menu.repository;

import com.sparta.outsourcing.domain.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT c FROM Menu c JOIN FETCH c.store WHERE c.store.id = :storeId AND c.deleted = false")
    List<Menu> findByStoreId(@Param("storeId") Long storeId);

    Optional<Menu> findMenuById(Long menuId);

    // 메뉴 이름으로 메뉴 찾기 추가 (기혜)
    Optional<Menu> findByName(String menuName);
    Page<Menu> findByNameContaining(String menuName, Pageable pageable);
}
