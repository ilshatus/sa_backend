package com.idc.idc.service;

import com.idc.idc.model.Task;
import com.idc.idc.model.users.Driver;

public interface TaskService {
    Task submitTask(Task task);
    Task getTask(Long taskId);
    Task getDriverTasks(Driver driver);

}
