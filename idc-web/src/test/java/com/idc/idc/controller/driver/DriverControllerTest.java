package com.idc.idc.controller.driver;

import com.idc.idc.WebApplication;
import com.idc.idc.model.users.Driver;
import com.idc.idc.repository.DriverRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class DriverControllerTest {
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
    public void getProfileWithCorrectId() throws Exception {
        mockMvc.perform(get("/v1/driver")
                .contentType("application/json")
                .accept("application/json")
                .header( "Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyX3R5cGUiOiJEUklWRVIiLCJ1c2VyX2lkI" +
                        "jo4LCJ0b2tlbl9leHBpcmF0aW9uX2RhdGUiOjE1NDM1OTc5NTAwODQsInRva2VuX2NyZWF0ZV9kYXRlIjoxNTQxMDA1OTUw" +
                        "MDgyfQ.v1wJzDpUYvkjnszdfduPePOZShiRcXuT1iC62GDliGIfLh3Rrurvh9VMbDBZ2WXY_W1KmJl7c8oAoVxrC_rRYw"))

                .andExpect(status().isOk());
    }

    @Test
    public void setLocationCorrectly() throws Exception {
        mockMvc.perform(post("/v1/driver/location")
                .contentType("application/json")
                .accept("application/json")
                .header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyX3R5cGUiOiJEUklWRVIiLCJ1c2VyX2" +
                        "lkIjo4LCJ0b2tlbl9leHBpcmF0aW9uX2RhdGUiOjE1NDM1OTc5NTAwODQsInRva2VuX2NyZWF0ZV9kYXRlIjoxNTQx" +
                        "MDA1OTUwMDgyfQ.v1wJzDpUYvkjnszdfduPePOZShiRcXuT1iC62GDliGIfLh3Rrurvh9VMbDBZ2WXY_W1KmJl7c8" +
                        "oAoVxrC_rRYw")
                .content("{\n" +
                        "  \"latitude\": 0,\n" +
                        "  \"longitude\": 0\n" +
                        "}"))
                .andExpect(status().isOk());
    }

//    @Test
//    public void sendLocation() throws Exception {
//        mockMvc.perform(post("/v1/driver/login")
//                .contentType("application/json")
//                .accept("application/json")
//                .content("{\n" +
//                        "\"email\": \"@test.com\",\n" +
//                        "\"firebase_token\": \"token\",\n" +
//                        "\"password\": \"qwerty\"\n" +
//                        "}"))
//                .andExpect(status().isUnauthorized());
//    }
}