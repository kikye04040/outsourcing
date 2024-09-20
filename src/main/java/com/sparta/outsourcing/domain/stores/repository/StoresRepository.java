package com.sparta.outsourcing.domain.stores.repository;

import com.sparta.outsourcing.domain.stores.entity.Stores;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoresRepository extends JpaRepository<Stores, Long> {
    Page<Stores> findByNameContaining(String keyword, Pageable pageable);

    // 특정 사용자 ID로 가게 개수 카운트
    @Query("SELECT COUNT(s) FROM Stores s WHERE s.user.id = :userId")
    int countByUserId(@Param("userId") Long userId);
}
