package com.idc.idc.repository;

import com.idc.idc.model.users.Operator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperatorRepository extends JpaRepository<Operator, Long> {
    Optional<Operator> findOneById(long id);

    Optional<Operator> findOneByEmail(String email);
}
