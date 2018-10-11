package com.idc.idc.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.Order;
import com.idc.idc.model.Task;
import com.idc.idc.model.enums.TaskStatus;
import com.idc.idc.model.users.Customer;
import com.idc.idc.model.users.Driver;
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

    private Order order;

    private Driver driver;

    private TaskStatus status;

    public static TaskJson mapFromTask(Task Task) {

        return TaskJson.builder()
                .id(Task.getId())
                .order(Task.getOrder())
                .driver(Task.getDriver())
                .status(Task.getStatus())
                .build();
    }
}
