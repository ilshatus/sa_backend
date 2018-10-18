package com.idc.idc.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class VehicleJson {

    private Long id;

    private VehicleType type;

    @JsonProperty("id_number")
    private String idNumber;

    private String model;

    private List<DriverJson> drivers;

    @JsonProperty("current_location")
    private CurrentLocationJson currentLocation;

    public static VehicleJson mapFromVehicle(Vehicle vehicle) {
        return VehicleJson.builder()
                .id(vehicle.getId())
                .type(vehicle.getType())
                .idNumber(vehicle.getIdNumber())
                .model(vehicle.getModel())
                .drivers(vehicle.getDrivers() != null ?
                    vehicle.getDrivers().stream().map(DriverJson::mapFromDriver).collect(Collectors.toList()) : null
                )
                .currentLocation(CurrentLocationJson.mapFromCurrentLocation(vehicle.getLocation()))
                .build();
    }

}
