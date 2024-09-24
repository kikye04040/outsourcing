package com.sparta.outsourcing.domain.kakao.entity;

import com.sparta.outsourcing.domain.user.entity.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "kakao_user")
public class KakaoUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String socialId; // Kakao에서 제공하는 고유 ID

    @Column(nullable = false)
    private String socialType; // "kakao"로 설정

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public KakaoUser(String socialId, String socialType, String email, String nickname, Role role) {
        this.socialId = socialId;
        this.socialType = socialType;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }
}
