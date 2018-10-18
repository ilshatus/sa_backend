package com.idc.idc.dto.json;

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

    public static DriverJson mapFromDriver(Driver Driver) {

        return DriverJson.builder()
                .id(Driver.getId())
                .email(Driver.getEmail())
                .name(Driver.getName())
                .build();
    }
}
