package com.sparta.outsourcing.domain.menu.entity;

import com.sparta.outsourcing.domain.menu.dto.request.MenuCreateRequestDto;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String menuPictureUrl;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId", nullable = false)
    private Stores store;


    public Menu(String menuPictureUrl, String name, String description, Integer price, Stores store) {
        if (price < 0) {
            throw new IllegalArgumentException("가격 음수 등록 불가");
        }

        this.menuPictureUrl = menuPictureUrl;
        this.name = name;
        this.description = description;
        this.price = price;
        this.store = store;
    }

    public void updateMenu(String menuPictureUrl, String name , String description, Integer price) {
        if (price < 0) {
            throw new IllegalArgumentException("가격 음수 등록 불가");
        }

        if (this.deleted) {
            throw new IllegalStateException("이미 삭제된 메뉴입니다");
        }

        this.menuPictureUrl = menuPictureUrl;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public void deleteMenu() {
        if (this.deleted) {
            throw new IllegalStateException("이미 삭제된 메뉴입니다");
        }

        this.deleted = true;
    }


}
