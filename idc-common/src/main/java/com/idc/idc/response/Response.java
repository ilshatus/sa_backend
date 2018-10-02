package com.idc.idc.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Response<T> {
    private T result;
    private String error;

    public Response(T result) {
        this.result = result;
    }
}