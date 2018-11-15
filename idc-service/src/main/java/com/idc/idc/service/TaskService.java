package com.idc.idc.service;

import com.idc.idc.dto.form.CreateTaskForm;
import com.idc.idc.model.Order;
import com.idc.idc.model.Task;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.enums.TaskStatus;
import com.idc.idc.model.users.Driver;

import java.util.List;

public interface TaskService {
    Task getTask(Long taskId);
    List<Task> getNotCompleteTasks(Integer limit, Integer offset);
    List<Task> getTasksByVehicle(Vehicle vehicle);
    List<Task> getTasksByStatus(TaskStatus status);
    List<Task> getTasksByVehicleAndStatus(Vehicle vehicle, TaskStatus status);
    List<Task> getTasksByDriverAndStatus(Long driverId, TaskStatus status);
    Task changeStatus(Long taskId, TaskStatus status, Long driverId);
    Task submitTask(Task task);
    Task getTaskByOrderAndStatus(Order order, TaskStatus status);
    Task createTask(Long orderId, CreateTaskForm form);
    void updatePositionOfVehicle(Long vehicleId);
}
