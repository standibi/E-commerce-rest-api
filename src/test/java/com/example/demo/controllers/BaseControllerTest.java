package com.example.demo.controllers;

import com.example.demo.model.persistence.User;

public class BaseControllerTest {
    protected User getUser() {
        User user = new User();

        user.setPassword("hash");
        user.setUsername("stanislas");
        return user;
    }
}
