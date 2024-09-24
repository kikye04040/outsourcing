package com.sparta.outsourcing.domain.user.repository;

import com.sparta.outsourcing.domain.user.entity.Status;
import com.sparta.outsourcing.domain.user.entity.User;
import com.sparta.outsourcing.domain.user.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndStatus(String email, Status status);

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    default User findByEmailOrElseThrow(String email) {
        return this.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    default User findByIdOrElseThrow(Long userId) {
        return findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
