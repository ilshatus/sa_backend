package com.idc.idc.util;

import com.idc.idc.dto.json.ValidationErrorJson;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class ValidationUtils {
    public static List<ValidationErrorJson> getValidationErrors(BindingResult bindingResult) {
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        List<ValidationErrorJson> validationErrorJsonList = new ArrayList<>();

        for (ObjectError error : allErrors) {
            String validationCode = error.getDefaultMessage();
            String errorField = error.getCode();
            validationErrorJsonList.add(new ValidationErrorJson(validationCode, errorField));
        }

        return validationErrorJsonList;
    }
}
