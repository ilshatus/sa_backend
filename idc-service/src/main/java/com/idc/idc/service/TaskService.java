package com.idc.idc.service;

import com.idc.idc.model.Order;
import com.idc.idc.model.Task;
import com.idc.idc.model.enums.TaskStatus;
import com.idc.idc.model.users.Driver;

import java.util.List;

public interface TaskService {
    Task getTask(Long taskId);
    List<Task> getTasksByDriver(Driver driver);
    List<Task> getTasksByStatus(TaskStatus status);
    Task submitTask(Task task);
    Task getTaskByOrderAndStatus(Order order, TaskStatus status);
}
