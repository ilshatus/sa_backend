package com.idc.idc.repository;

import com.idc.idc.model.Order;
import com.idc.idc.model.Task;
import com.idc.idc.model.enums.TaskStatus;
import com.idc.idc.model.users.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findById(Long id);
    List<Task> findAllByDriver(Driver driver);
    List<Task> findAllByStatus(TaskStatus status);
    Optional<Task> findByOrderAndStatus(Order order, TaskStatus taskStatus);
}
