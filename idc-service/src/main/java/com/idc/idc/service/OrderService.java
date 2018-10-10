package com.idc.idc.service;

import com.idc.idc.model.Order;

import java.util.List;

public interface OrderService {
    Order getOrder(Long orderId);
    List<Order> getListOfOrders();
}
