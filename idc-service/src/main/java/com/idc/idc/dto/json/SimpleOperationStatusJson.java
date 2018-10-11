package com.idc.idc.dto.json;

import com.google.common.collect.Lists;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class SimpleOperationStatusJson {
    @Column(name = "is_success")
    private boolean success = Boolean.FALSE;

    private List<ValidationErrorJson> errors;

    public List<ValidationErrorJson> getErrors() {
        if (errors == null) {
            errors = Lists.newArrayList();
        }
        return errors;
    }
}
