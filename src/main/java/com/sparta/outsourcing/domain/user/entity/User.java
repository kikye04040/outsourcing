package com.sparta.outsourcing.domain.user.entity;

import com.sparta.outsourcing.domain.stores.entity.Stores;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE user SET status = 'WITHDRAWN' WHERE user_id = ?")
@SQLRestriction("status != 'WITHDRAWN'")
@Entity
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String password;

    private String phone;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    private Grade grade;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private String currentAddress;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stores> stores;

    @Builder
    private User(String email, String password, String phone, String name, Role role, Grade grade, Status status, String currentAddress, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.role = role;
        this.grade = grade;
        this.status = status;
        this.currentAddress = currentAddress;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
