package com.idc.idc.service;

import com.idc.idc.model.Order;
import com.sun.mail.iap.Response;

import java.util.List;


public interface OrderService {
    Order getOrder(Long orderId);
    List<Order> getListOfOrders();
    String createOrder(Order order);
}
