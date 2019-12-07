package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;

public class BaseControllerTest {
    protected User getUser() {
        User user = new User();

        user.setPassword("hash");
        user.setUsername("stanislas");
        user.setCart(new Cart());
        return user;
    }
}
