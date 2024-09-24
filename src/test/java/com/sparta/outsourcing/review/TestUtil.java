package com.sparta.outsourcing.review;

import com.sparta.outsourcing.domain.menu.entity.Menu;
import com.sparta.outsourcing.domain.order.entity.Order;
import com.sparta.outsourcing.domain.stores.entity.Stores;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    public static User createMockUser() {
        User user = User.builder()
                .email("email")
                .password("password")
                .phone("010-0000-0000")
                .name("name")
                .role(Role.ROLE_OWNER)
                .currentAddress("address")
                .build();
        return user;
    }

    public static Order createMockOrder(User user, Stores stores) {
        List<Menu> menus = new ArrayList<>();
        return new Order(stores, menus, user, 0);
    }

    public static Stores createMockStores(User user) {
        return new Stores("", 0, "", "", "", "" ,"", 0, "", "",user,0);
    }
}
