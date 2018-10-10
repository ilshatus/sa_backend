package com.idc.idc.repository;


import com.idc.idc.model.Order;
import com.idc.idc.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOneById(long id);
    List<Order> findAllByStatus(OrderStatus status);
}
