package com.idc.idc.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.embeddable.CurrentLocation;
import com.idc.idc.model.enums.VehicleType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateVehicleForm {
    private static final String VEHICLE_TYPE = "vehicle_type";
    private static final String ID_NUMBER = "id_number";
    private static final String MODEL = "model";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    @NotNull(message = "The vehicle type must not be null")
    @JsonProperty(value = VEHICLE_TYPE, required = true)
    private VehicleType type;

    @NotBlank(message = "The identification number must not be empty")
    @JsonProperty(value = ID_NUMBER, required = true)
    private String idNumber;

    @NotBlank(message = "The model must not be empty")
    @JsonProperty(value = MODEL, required = true)
    private String model;

    @NotNull(message = "The latitude must not be null")
    @JsonProperty(value = LATITUDE, required = true)
    private Double latitude;

    @NotNull(message = "The longitude must not be null")
    @JsonProperty(value = LONGITUDE, required = true)
    private Double longitude;

    public Vehicle toVehicle() {
        return Vehicle.builder()
                .type(type)
                .idNumber(idNumber)
                .model(model)
                .location(new CurrentLocation(latitude, longitude))
                .build();
    }
}
