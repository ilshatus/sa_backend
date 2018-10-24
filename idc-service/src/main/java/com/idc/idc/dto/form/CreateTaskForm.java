package com.idc.idc.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.Task;
import com.idc.idc.model.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateTaskForm {
    public static final String VEHICLE_ID = "vehicle_id";
    public static final String ROUTE_ID = "route_id";

    @NotNull(message = "The vehicle id must not be null")
    @JsonProperty(value = VEHICLE_ID, required = true)
    private Long vehicleId;

    @NotNull(message = "The route id must not be null")
    @JsonProperty(value = ROUTE_ID, required = true)
    private Long routeId;

    public Task toTask() {
        return Task.builder()
                .status(TaskStatus.PENDING)
                .build();
    }
}
