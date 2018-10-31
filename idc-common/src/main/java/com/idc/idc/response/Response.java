package com.idc.idc.response;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.compress.utils.Lists;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;

@Setter
@Getter
public class Response<T> {
    private T result;
    private Error error;

    public Response(T result) {
        this.result = result;
    }

    public Response(T result, Errors errors) {
        this.result = result;
        this.error = new Error(errors);
    }

    public Response(T result, String error) {
        this.result = result;
        this.error = new Error(error);
    }

    @Getter
    @Setter
    public class Error {
        private List<String> errors;

        public Error(String error) {
            this.errors = Lists.newArrayList();
            this.errors.add(error);
        }

        public Error(Errors errors) {
            List<ObjectError> allErrors = errors.getAllErrors();
            this.errors = Lists.newArrayList();
            for (ObjectError error : allErrors) {
                this.errors.add(error.getDefaultMessage());
            }
        }
    }
}