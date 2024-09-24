package com.sparta.outsourcing.domain.stores.repository;

import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.stores.entity.StoresLike;
import com.sparta.outsourcing.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoresLikeRepository extends JpaRepository<StoresLike, Long> {
    Optional<StoresLike> findByUserAndStores(User user, Stores store);
}
