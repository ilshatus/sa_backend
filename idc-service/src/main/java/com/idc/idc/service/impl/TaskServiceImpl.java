package com.idc.idc.service.impl;

import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Order;
import com.idc.idc.model.Task;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.enums.TaskStatus;
import com.idc.idc.model.users.Driver;
import com.idc.idc.repository.TaskRepository;
import com.idc.idc.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(String.format("Task %d not found", taskId)));
    }

    @Override
    public List<Task> getTasksByDriver(Vehicle vehicle) {
        return taskRepository.findAllByVehicle(vehicle);
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findAllByStatus(status);
    }

    @Override
    public Task submitTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task getTaskByOrderAndStatus(Order order, TaskStatus status) {
        return taskRepository.findByOrderAndStatus(order, status)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Task for order %s and with status %s not found", order.getId(), status)));
    }
}
