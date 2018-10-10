package com.idc.idc.service.impl;

import com.idc.idc.model.Order;
import com.idc.idc.model.Task;
import com.idc.idc.model.enums.TaskStatus;
import com.idc.idc.model.users.Driver;
import com.idc.idc.service.TaskService;

import java.util.Set;

public class TaskServiceImpl implements TaskService {
    @Override
    public Task submitTask(Task task) {
        return ;
    }

    @Override
    public Task getTask(Long taskId) {
        return null;
    }

    @Override
    public Set<Task> getTasksByDriver(Driver driver) {
        return null;
    }

    @Override
    public Set<Task> getTasksByStatus(TaskStatus status) {
        return null;
    }

    @Override
    public Set<Task> getTasksByOrder(Order order) {
        return null;
    }
}
