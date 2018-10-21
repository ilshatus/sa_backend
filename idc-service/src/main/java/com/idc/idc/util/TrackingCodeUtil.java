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

    public static final int TRACKING_CODE_LENGTH = 8;

    public String generate() {
        return generate(TRACKING_CODE_LENGTH);
    }

    public String generate(int length)
    {
        String origin = countries[0];
        String destination = countries[0];
        String trackingCode = origin + RandomStringUtils.randomAlphanumeric(length) + destination;
        while (orderRepository.findOneByTrackingCode(trackingCode).isPresent()) {
            trackingCode = origin + RandomStringUtils.randomAlphanumeric(length) + destination;
        }
        return trackingCode;
    }

}
