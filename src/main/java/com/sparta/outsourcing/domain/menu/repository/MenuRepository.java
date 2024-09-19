package com.sparta.outsourcing.domain.menu.repository;

import com.sparta.outsourcing.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT c FROM Menu c JOIN FETCH c.store WHERE c.store.storeId = :storeId AND c.deleted = false")
    List<Menu> findByStoreId(@Param("storeId") Long storeId);

    Optional<Menu> findByMenuId(Long menuId);

}
