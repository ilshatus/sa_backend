package com.idc.idc.dto.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenJson {
    
    @JsonProperty("auth_token")
    String token;

    private Long userId;
}
