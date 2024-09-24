package com.sparta.outsourcing.domain.kakao.dto;

import com.sparta.outsourcing.domain.kakao.entity.KakaoUser;
import com.sparta.outsourcing.domain.user.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KakaoUserDetails implements UserDetails {

    private final KakaoUser kakaoUser;

    public KakaoUserDetails(KakaoUser kakaoUser) {
        this.kakaoUser = kakaoUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(kakaoUser.getRole().name())); // 기본 역할 추가

        return authorities;
    }

    public String getEmail() {
        return kakaoUser.getEmail();
    }

    @Override
    public String getUsername() {
        return kakaoUser.getEmail(); // 이메일을 사용자 이름으로 사용
    }

    @Override
    public String getPassword() {
        return null; // KakaoUser의 비밀번호가 없으므로 null을 반환합니다.
    }

    public String getNickname() {
        return kakaoUser.getNickname(); // 닉네임 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
