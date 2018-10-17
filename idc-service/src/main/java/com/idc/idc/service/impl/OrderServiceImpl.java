package com.idc.idc.service.impl;

import com.idc.idc.dto.form.OrderCreationForm;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Order;
import com.idc.idc.model.embeddable.CurrentLocation;
import com.idc.idc.model.embeddable.OrderDestination;
import com.idc.idc.model.embeddable.OrderOrigin;
import com.idc.idc.model.enums.OrderStatus;
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
    private UserService userService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
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

    @Override
    public Order changeStatus(Long orderId, OrderStatus status) {
        if (orderId == null) {
            return null;
        }
        Order order = getOrder(orderId);
        order.setStatus(status);
        return submitOrder(order);
    }

    @Override
    public Order createOrder(OrderCreationForm form, Long customerId) {
        Order order = form.toOrder();
        order.setStatus(OrderStatus.PENDING_CONFIRMATION);
        order.setCustomer(userService.getCustomer(customerId));
        order.setDeliverPrice(calculatePrice(order));
        return submitOrder(order);
    }

    private Long calculatePrice(Order order) {
        double R = 6371e3; // metres
        double lat1 = order.getOrigin().getOriginLatitude() * Math.PI / 180;
        double lat2 = order.getDestination().getDestinationLatitude() * Math.PI / 180;
        double lon1 = order.getOrigin().getOriginLongitude() * Math.PI / 180;
        double lon2 = order.getDestination().getDestinationLongitude() * Math.PI / 180;

        double deltaF = lat2 - lat1;
        double deltaLambda = lon2 - lon1;

        double a = Math.sin(deltaF / 2) * Math.sin(deltaF / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return (long) ((order.getWeight() * 0.2) * (distance * 0.1) * (order.getWorth() * 0.5));
    }
}
