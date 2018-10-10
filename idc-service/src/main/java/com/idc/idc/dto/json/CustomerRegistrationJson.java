package com.idc.idc.dto.json;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerRegistrationJson extends SimpleOperationStatusJson {
    private TokenJson token;
}
