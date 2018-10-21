package com.idc.idc.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.embeddable.CurrentLocation;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LocationForm {
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "latitude";

    @NotNull(message = "The latitude must not be null")
    @JsonProperty(value = LATITUDE, required = true)
    private Double latitude;


    @NotNull(message = "The longitude must not be null")
    @JsonProperty(value = LONGITUDE, required = true)
    private Double longitude;

    public CurrentLocation toCurrentLocation() {
        return new CurrentLocation(latitude, longitude);
    }
}
