package com.idc.idc.repository;

import com.idc.idc.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
        Optional<Task> findOneById(long id);
}