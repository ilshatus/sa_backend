package com.idc.idc.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class SignInForm {
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    @NotBlank(message = "The email must not be empty")
    @JsonProperty(value = EMAIL, required = true)
    private String email;

    @NotBlank(message = "The password must not be empty")
    @JsonProperty(value = PASSWORD, required = true)
    private String password;

    @JsonProperty(value = "firebase_token")
    private String firebaseToken;
}
