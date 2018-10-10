package com.idc.idc.service;

import com.idc.idc.model.Order;
import com.idc.idc.model.Task;
import com.idc.idc.model.enums.TaskStatus;
import com.idc.idc.model.users.Driver;

import java.util.Set;

public interface TaskService {
    Task submitTask(Task task);
    Task getTask(Long taskId);
    Set<Task> getTasksByDriver(Driver driver);
    Set<Task> getTasksByStatus(TaskStatus status);
    Set<Task> getTasksByOrder(Order order);
}
