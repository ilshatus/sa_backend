package com.idc.idc.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.users.Customer;
import com.idc.idc.model.users.Driver;
import com.idc.idc.model.users.Operator;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

import static com.idc.idc.validator.ValidationConstants.*;

@Getter
@Setter
public class UserRegistrationForm {
    public static final String NAME = "name"; // validate
    public static final String EMAIL = "email"; // validate
    public static final String PASSWORD_FIELD = "password"; // validate
    public static final String PASSWORD_CONFIRM_FIELD = "password_confirm"; // validate

    @JsonProperty(value = NAME, required = true)
    @NotBlank(message = "The name must not be blank")
    private String name;

    @JsonProperty(value = EMAIL, required = true)
    @NotBlank(message = "The email must not be blank")
    @Email(message = "Email format is wrong")
    private String email;

    @JsonProperty(value = PASSWORD_FIELD, required = true)
    @NotBlank(message = "The password must not be blank")
    @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_VALIDATION_MESSAGE)
    private String password;

    @JsonProperty(value = PASSWORD_CONFIRM_FIELD, required = true)
    @NotBlank(message = "The password confirm must not be blank")
    private String passwordConfirm;

    public Customer toCustomer() {
        return Customer
                .builder().name(name)
                .email(email.toLowerCase())
                .build();
    }

    public Driver toDriver() {
        return Driver
                .builder()
                .name(name)
                .email(email.toLowerCase())
                .build();
    }

    public Operator toOperator() {
        return Operator
                .builder()
                .name(name)
                .email(email.toLowerCase())
                .build();
    }
}
