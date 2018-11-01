package com.idc.idc.dto.json;

import com.idc.idc.model.users.Driver;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SimpleDriverJson {
    private Long id;

    private String name;

    public static SimpleDriverJson mapFromDriver(Driver Driver) {

        return SimpleDriverJson.builder()
                .id(Driver.getId())
                .name(Driver.getName())
                .build();
    }
}
