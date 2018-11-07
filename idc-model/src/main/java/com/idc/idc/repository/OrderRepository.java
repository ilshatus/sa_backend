package com.idc.idc.repository;


import com.idc.idc.model.Order;
import com.idc.idc.model.enums.OrderStatus;
import com.idc.idc.model.users.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOneById(long id);
    Optional<Order> findOneByTrackingCode(String trackingCode);
    List<Order> findAllByStatus(OrderStatus status);
    List<Order> findAllByCustomer(Customer customer, Pageable pageable);
}
