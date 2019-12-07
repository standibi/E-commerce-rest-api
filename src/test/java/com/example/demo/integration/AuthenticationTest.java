package com.example.demo.integration;

import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.SecurityConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class AuthenticationTest {

    Logger log = LoggerFactory.getLogger(AuthenticationTest.class);
    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<LoginRequest> json;

    private String token;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void test() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                post(new URI("/login"))
                    .content(json.write(new LoginRequest("user", "password")).getJson()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        log.info(response.toString());

    }

    @Test
    public void user_can_register() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setPassword("stanislas");
        createUserRequest.setPassword_confirmation("stanislas");
        createUserRequest.setUsername("dibi");

        mvc.perform(
                post(new URI("/api/user/create"))
                        .content(mapper.writeValueAsString(createUserRequest))).andExpect(status().isOk());
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
}

class LoginRequest{
    @JsonProperty
    private String username;
    @JsonProperty
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
