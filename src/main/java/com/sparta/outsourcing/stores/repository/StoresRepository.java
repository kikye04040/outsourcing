package com.sparta.outsourcing.stores.repository;

import com.sparta.outsourcing.stores.entity.Stores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoresRepository extends JpaRepository<Stores, Long> {
}
