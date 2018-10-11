package com.idc.idc.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.users.Operator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OperatorJson {

    private Long id;

    private String email;

    private String name;

    private Boolean admin;

    public static OperatorJson mapFromOperator(Operator Operator) {

        return OperatorJson.builder()
                .id(Operator.getId())
                .email(Operator.getEmail())
                .name(Operator.getName())
                .admin(Operator.getAdmin())
                .build();
    }
}
