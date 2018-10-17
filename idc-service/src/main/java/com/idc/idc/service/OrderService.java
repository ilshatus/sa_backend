package com.idc.idc.service;

import com.idc.idc.dto.form.OrderCreationForm;
import com.idc.idc.model.Order;
import com.idc.idc.model.embeddable.CurrentLocation;
import com.idc.idc.model.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    Order getOrder(Long orderId);
    Order getOrder(String trackingCode);
    List<Order> getListOfOrders();
    Order submitOrder(Order order);
    Order changeStatus(Long orderId, OrderStatus status);
    Order createOrder(OrderCreationForm form, Long customerId);
}
