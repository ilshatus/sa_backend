package com.idc.idc.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.embeddable.CurrentLocation;
import com.idc.idc.model.users.Driver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DriverJson {

    private Long id;

    private String email;

    private String name;

    @JsonProperty("current_location")
    private CurrentLocation location;

    public static DriverJson mapFromDriver(Driver Driver) {

        return DriverJson.builder()
                .id(Driver.getId())
                .email(Driver.getEmail())
                .name(Driver.getName())
                .location(Driver.getLocation())
                .build();
    }
}
