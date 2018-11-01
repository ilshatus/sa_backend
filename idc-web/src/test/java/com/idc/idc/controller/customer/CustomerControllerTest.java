package com.idc.idc.controller.customer;

import com.idc.idc.WebApplication;
import com.idc.idc.repository.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
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
public class CustomerControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CustomerRepository customerRepository;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).dispatchOptions(true).build();
    }
    @Test
    public void createOrder() throws Exception {
        mockMvc.perform(post("/v1/customer/order")
                .contentType("application/json")
                .accept("application/json")
                .content("{\n" +
                        "  \"description\": \"string\",\n" +
                        "  \"destination_full_address\": \"string\",\n" +
                        "  \"destination_lat\": 0,\n" +
                        "  \"destination_long\": 0,\n" +
                        "  \"destination_short_address\": \"string\",\n" +
                        "  \"dueDate\": 0,\n" +
                        "  \"origin_full_address\": \"string\",\n" +
                        "  \"origin_lat\": 0,\n" +
                        "  \"origin_long\": 0,\n" +
                        "  \"origin_short_address\": \"string\",\n" +
                        "  \"weight\": 0,\n" +
                        "  \"worth\": 0\n" +
                        "}"))
                .andExpect(status().isOk());
    }


}