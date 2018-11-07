package com.idc.idc.dto.json;

import com.idc.idc.model.Task;
import com.idc.idc.model.enums.TaskStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SimpleTaskJson {
    private Long id;

    private OrderJson order;

    private Long routeId;

    private TaskStatus status;

    public static SimpleTaskJson mapFromTask(Task task) {
        if (task == null) return null;
        return SimpleTaskJson.builder()
                .id(task.getId())
                .order(OrderJson.mapFromOrder(task.getOrder()))
                .routeId(task.getRouteId())
                .status(task.getStatus())
                .build();
    }
}
