package com.sparta.outsourcing.domain.stores.repository;

import com.sparta.outsourcing.domain.stores.entity.Stores;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoresRepository extends JpaRepository<Stores, Long> {
    Page<Stores> findByNameContaining(String keyword, Pageable pageable);

    int countByUser_Email(String email);
}
