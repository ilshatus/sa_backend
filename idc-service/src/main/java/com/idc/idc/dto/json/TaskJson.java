package com.idc.idc.dto.json;

import com.idc.idc.model.Task;
import com.idc.idc.model.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TaskJson {

    private Long id;

    private OrderJson order;

    private VehicleJson vehicle;

    private TaskStatus status;

    public static TaskJson mapFromTask(Task task) {

        return TaskJson.builder()
                .id(task.getId())
                .order(OrderJson.mapFromOrder(task.getOrder()))
                .vehicle(VehicleJson.mapFromVehicle(task.getVehicle()))
                .status(task.getStatus())
                .build();
    }
}
