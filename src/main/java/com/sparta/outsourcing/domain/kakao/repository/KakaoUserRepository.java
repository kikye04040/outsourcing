package com.sparta.outsourcing.domain.kakao.repository;

import com.sparta.outsourcing.domain.kakao.entity.KakaoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KakaoUserRepository extends JpaRepository<KakaoUser, Long> {
    Optional<KakaoUser> findBySocialIdAndSocialType(String socialId, String kakao);
}
