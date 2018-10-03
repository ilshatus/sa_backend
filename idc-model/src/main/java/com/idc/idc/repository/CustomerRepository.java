package com.idc.idc.repository;


import com.idc.idc.model.users.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findOneById(long id);

    Optional<Customer> findOneByEmail(String email);
}
