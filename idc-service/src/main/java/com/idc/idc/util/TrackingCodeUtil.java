package com.idc.idc.util;

import com.idc.idc.repository.OrderRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TrackingCodeUtil {

    private OrderRepository orderRepository;

    public TrackingCodeUtil(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private final String countries[] = new String[]{"RU", "KZ", "DE", "GB"};

    public static final int DEFAULT_PASSWORD_LENGTH = 8;

    public String generate() {
        return generate(DEFAULT_PASSWORD_LENGTH);
    }

    public String generate(int length)
    {

        Random rand = new Random();
        String origin = countries[rand.nextInt() % 6];
        String destination = countries[rand.nextInt() % 6];
        String trackingCode = origin + RandomStringUtils.randomAlphanumeric(length) + destination;
        while (orderRepository.findOneByTrackingCode(trackingCode).isPresent()) {
            trackingCode = origin + RandomStringUtils.randomAlphanumeric(length) + destination;
        }
        return trackingCode;
    }

}
