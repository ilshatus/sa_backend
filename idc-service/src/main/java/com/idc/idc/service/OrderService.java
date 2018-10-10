package com.idc.idc.service;

import com.idc.idc.model.Order;
import com.idc.idc.model.users.Driver;

import java.util.List;

public interface OrderService {
    Order getOrder(Long orderId);
    List<Order> getListOfOrders();
    Order submitOrder(Order order);
    List<Driver> getNearestDrivers(Order order, Integer limit);
}
