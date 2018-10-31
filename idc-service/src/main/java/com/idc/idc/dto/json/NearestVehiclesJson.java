package com.idc.idc.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class NearestVehiclesJson {
    @JsonProperty("vehicle_json")
    private VehicleJson vehicleJson;

    @JsonProperty("time_till_order")
    private Long timeTillOrder;
}
