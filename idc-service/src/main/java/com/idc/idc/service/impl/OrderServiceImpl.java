package com.idc.idc.service.impl;

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


    private double calculatePrice(double weight, double worth, OrderOrigin origin, OrderDestination destination){
        double R = 6371e3; // metres
        double lat1 = origin.getOriginLatitude()*Math.PI/180;
        double lat2 = destination.getDestinationLatitude()*Math.PI/180;
        double lon1 = origin.getOriginLongitude()*Math.PI/180;
        double lon2 = destination.getDestinationLongitude()*Math.PI/180;

        double delta_f = lat2-lat1;
        double delta_lammbda = lon2-lon1;

        double a = Math.sin(delta_f/2) * Math.sin(delta_f/2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(delta_lammbda/2) * Math.sin(delta_lammbda/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;

        return (weight*0.2)*(distance*0.1)*(worth*0.5);
    }

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
    public List<Driver> getNearestDrivers(Order order, Integer limit) {
        List<Driver> drivers = userService.getAllDrivers();
        OrderOrigin orderLoc = order.getOrigin();
        drivers.sort((Driver o1, Driver o2) -> {
            CurrentLocation loc1 = o1.getLocation();
            CurrentLocation loc2 = o2.getLocation();
            Double dist1 = Math.pow(loc1.getLatitude() - orderLoc.getOriginLatitude(), 2) +
                    Math.pow(loc1.getLongitude() - orderLoc.getOriginLongitude(), 2);

            Double dist2 = Math.pow(loc2.getLatitude() - orderLoc.getOriginLatitude(), 2) +
                    Math.pow(loc2.getLongitude() - orderLoc.getOriginLongitude(), 2);
            if (dist1.equals(dist2))
                return 0;
            if (dist1 < dist2) {
                return 1;
            } else {
                return -1;
            }
        });
        return CollectionUtils.subList(drivers, 0, limit);
    }

    @Override
    public Order changeStatus(Long orderId, OrderStatus status){
        if (orderId == null) {
            return null;
        }
        Optional<Order> oneById = orderRepository.findOneById(orderId);
        oneById.get().setStatus(status);
        return oneById.orElseThrow(() -> new NotFoundException(String.format("Order %d not found", orderId)));
    }


}
