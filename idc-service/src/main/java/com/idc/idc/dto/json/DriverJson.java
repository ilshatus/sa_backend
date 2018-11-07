package com.idc.idc.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("avatar_url")
    private String avatarUrl;

    public static DriverJson mapFromDriver(Driver driver) {

        return DriverJson.builder()
                .id(driver.getId())
                .email(driver.getEmail())
                .name(driver.getName())
                .avatarUrl(driver.getAvatarUrl())
                .build();
    }
}
