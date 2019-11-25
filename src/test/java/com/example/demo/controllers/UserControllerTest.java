package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest extends BaseControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path(){
        when(encoder.encode("stanpass")).thenReturn("itWasHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setPassword("stanpass");
        createUserRequest.setPassword_confirmation("stanpass");
        createUserRequest.setUsername("stanislas");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertEquals("stanislas", user.getUsername());
        assertEquals("itWasHashed", user.getPassword());
    }

    @Test
    public void find_by_id(){
        User user = getUser();

        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        final ResponseEntity<User> response = userController.findById(user.getId());

        assertNotNull(response);
        assertEquals("stanislas", response.getBody().getUsername());
    }

    @Test
    public void find_by_userName(){
        User user = getUser();
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        final ResponseEntity<User> response = userController.findByUserName(user.getUsername());

        assertNotNull(response);
        assertEquals(user.getUsername(), response.getBody().getUsername());

    }



}
