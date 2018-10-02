package com.idc.idc.dto.json;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistrationJson extends SimpleOperationStatusJson {
    private TokenJson token;
}
