package com.example.demo.integration;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class AuthTest {

    Logger log = LoggerFactory.getLogger(AuthTest.class);
    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JacksonTester<LoginRequest> json;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private String token;

    private LoginRequest loginRequest;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public  void setUp(){
        User user = new User();
        user.setUsername("user");
        user.setPassword(encoder.encode("stanislas"));
        userRepository.save(user);
        loginRequest = new LoginRequest("user", "stanislas");
    }

    @After
    public void tearDown(){
        User user = userRepository.findByUsername("user");
        userRepository.delete(user);
    }

    @Test
    public void registered_user_can_login() throws Exception {
        login("user", "stanislas")
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
//                .andReturn()
//                .getResponse();

//        String authorizationHeader = response.getHeader("Authorization");
//        String[] s = authorizationHeader.split(" ");
//        System.out.println("token "+s[1]);

    }

    @Test
    public void user_can_register() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setPassword("stanislas");
        createUserRequest.setPassword_confirmation("stanislas");
        createUserRequest.setUsername("test_user");

        mvc.perform(
                post(new URI("/api/user/create"))
                        .header("Content-Type", "application/json")
                        .content(mapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void unauthenticated_user_cannot_submit_order() throws Exception {
        mvc.perform(
                post(new URI("/submit/stanislas")))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void unauthenticated_user_cannot_see_order_history() throws Exception {
        mvc.perform(
                post(new URI("/history/stanislas")))
                .andExpect(status().is4xxClientError());
    }

    private ResultActions login (String username, String password) throws Exception {
        final ResultActions perform = mvc.perform(
                post(new URI("/login"))
                        .content(mapper.writeValueAsString(new LoginRequest(username, password))));
        return perform;
    }
}

