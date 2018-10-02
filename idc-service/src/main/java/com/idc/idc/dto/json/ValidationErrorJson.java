package com.idc.idc.dto.json;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationErrorJson {
    private String code;
    private String field;
}
