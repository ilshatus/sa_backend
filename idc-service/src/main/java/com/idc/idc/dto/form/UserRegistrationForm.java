package com.idc.idc.dto.form;

import com.idc.idc.model.users.Customer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationForm {
    public static final String NAME = "name"; // validate
    public static final String EMAIL = "email"; // validate
    public static final String PASSWORD_FIELD = "password"; // validate
    public static final String PASSWORD_CONFIRM_FIELD = "password_confirm"; // validate

    @ApiModelProperty(value = NAME, required = true)
    private String name;

    @ApiModelProperty(value = EMAIL, required = true)
    private String email;

    @ApiModelProperty(value = PASSWORD_FIELD, required = true)
    private String password;

    @ApiModelProperty(value = PASSWORD_CONFIRM_FIELD, required = true)
    private String passwordConfirm;

    public Customer toUser() {
        return Customer
                .builder()
                .name(name)
                .email(email)
                .build();
    }
}
