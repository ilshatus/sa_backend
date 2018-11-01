package com.idc.idc.dto.json;

import com.idc.idc.model.users.Operator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SimpleOperatorJson {
    private Long id;

    private String name;

    public static SimpleOperatorJson mapFromOperator(Operator operator) {
        if (operator == null) return null;
        return SimpleOperatorJson.builder()
                .id(operator.getId())
                .name(operator.getName())
                .build();
    }
}
