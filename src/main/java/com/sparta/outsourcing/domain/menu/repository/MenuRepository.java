package com.sparta.outsourcing.domain.menu.repository;

import com.sparta.outsourcing.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    // 메뉴 이름으로 메뉴 찾기 추가 (기혜)
    Optional<Menu> findByName(String menuName);
}
