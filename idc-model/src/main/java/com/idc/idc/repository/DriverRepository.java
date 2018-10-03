package com.idc.idc.repository;

import com.idc.idc.model.users.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findOneById(long id);

    Optional<Driver> findOneByEmail(String email);
}
