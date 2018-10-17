package com.idc.idc.service.impl;

import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Order;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.embeddable.CurrentLocation;
import com.idc.idc.model.embeddable.OrderOrigin;
import com.idc.idc.model.users.Driver;
import com.idc.idc.repository.OrderRepository;
import com.idc.idc.service.OrderService;
import com.idc.idc.service.UserService;
import com.idc.idc.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order getOrder(Long orderId) {
        if (orderId == null) {
            return null;
        }
        Optional<Order> oneById = orderRepository.findOneById(orderId);
        return oneById.orElseThrow(() -> new NotFoundException(String.format("Order %d not found", orderId)));
    }

    @Override
    public List<Order> getListOfOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order submitOrder(Order order) {
        return orderRepository.save(order);
    }
}
