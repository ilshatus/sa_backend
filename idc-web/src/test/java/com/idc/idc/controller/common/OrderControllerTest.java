package com.idc.idc.controller.common;

import com.idc.idc.WebApplication;
import com.idc.idc.repository.OrderRepository;
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
//@TestPropertySource(locations="classpath:application.properties")
public class OrderControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private OrderRepository orderRepository;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).dispatchOptions(true).build();
    }
    @Test
    public void getLocation() throws Exception {
        mockMvc.perform(get("/v1/orders/v1/orders/track/RUcicY0IuVRU"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"result\":" +
                        "{\"id\":2,\"due_date\":1572437491200,\"origin\":" +
                        "{\"origin_latitude\":52.242552,\"origin_longitude\":21.003895,\"origin_full_address\"" +
                        ":\"Poland, Warszawa, Senatorska 2/28\",\"origin_short_address\"" +
                        ":\"Poland, Warszawas\"},\"destination\":{\"destination_latitude\"" +
                        ":54.989594,\"destination_longitude\":73.295343,\"destination_full_address\"" +
                        ":\"Russia, Omsk, Komarova 41/45\",\"destination_short_address\"" +
                        ":\"Russia, Omsk\"},\"status\":\"IN_PROGRESS\",\"weight\":15.0,\"worth\"" +
                        ":1400,\"description\":\"2 big suitcases with a Personal Computer\",\"locationJson\"" +
                        ":{\"latitude\":52.242552,\"longitude\":21.003895},\"deliver_price\"" +
                        ":710114815},\"error\":null}"));
    }
}