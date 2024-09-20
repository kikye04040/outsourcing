package com.sparta.outsourcing.domain.stores.repository;

import com.sparta.outsourcing.domain.stores.entity.Stores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoresRepository extends JpaRepository<Stores, Long> {
}
