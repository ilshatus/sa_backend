package com.idc.idc.service;

import com.idc.idc.model.Order;
import com.idc.idc.model.Task;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.enums.TaskStatus;

import java.util.List;

public interface TaskService {
    Task getTask(Long taskId);
    List<Task> getTasksByDriver(Vehicle vehicle);
    List<Task> getTasksByStatus(TaskStatus status);
    Task submitTask(Task task);
    Task getTaskByOrderAndStatus(Order order, TaskStatus status);
}
