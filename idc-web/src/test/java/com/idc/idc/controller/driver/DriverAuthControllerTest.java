package com.idc.idc.controller.driver;

import com.idc.idc.WebApplication;
import com.idc.idc.repository.DriverRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class DriverAuthControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DriverRepository driverRepository;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).dispatchOptions(true).build();
    }

    @Test
    public void correctSignIn() throws Exception {
        mockMvc.perform(post("/v1/driver/login")
                .contentType("application/json")
                .accept("application/json")
                .content("{\n" +
                        "\"email\": \"test@test.com\",\n" +
                        "\"firebase_token\": \"token\",\n" +
                        "\"password\": \"qwerty\"\n" +
                        "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void incorrectLoginSignIn() throws Exception {
        mockMvc.perform(post("/v1/driver/login")
                .contentType("application/json")
                .accept("application/json")
                .content("{\n" +
                        "\"email\": \"@test.com\",\n" +
                        "\"firebase_token\": \"token\",\n" +
                        "\"password\": \"qwerty\"\n" +
                        "}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void incorrectPasswordSignIn() throws Exception {
        mockMvc.perform(post("/v1/driver/login")
                .contentType("application/json")
                .accept("application/json")
                .content("{\n" +
                        "\"email\": \"@test.com\",\n" +
                        "\"firebase_token\": \"token\",\n" +
                        "\"password\": \"qwerty\"\n" +
                        "}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void signInWithoutFbToken() throws Exception {
        mockMvc.perform(post("/v1/driver/login")
                .contentType("application/json")
                .accept("application/json")
                .content("{\n" +
                        "\"email\": \"test@test.com\",\n" +
                        "\"password\": \"qwerty\"\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void signInWithoutEmail() throws Exception {
        mockMvc.perform(post("/v1/driver/login")
                .contentType("application/json")
                .accept("application/json")
                .content("{\n" +
                        "\"firebase_token\": \"token\",\n" +
                        "\"password\": \"qwerty\"\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void signInWithoutPassword() throws Exception {
        mockMvc.perform(post("/v1/driver/login")
                .contentType("application/json")
                .accept("application/json")
                .content("{\n" +
                        "\"email\": \"test@test.com\",\n" +
                        "\"firebase_token\": \"token\",\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }
}