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

    @JsonProperty("avatar_url")
    private String avatarUrl;

    public static OperatorJson mapFromOperator(Operator operator) {

        return OperatorJson.builder()
                .id(operator.getId())
                .email(operator.getEmail())
                .name(operator.getName())
                .admin(operator.getAdmin())
                .avatarUrl(operator.getAvatarUrl())
                .build();
    }
}
