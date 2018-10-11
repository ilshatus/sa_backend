package com.idc.idc.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.users.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CustomerJson {

    private Long id;

    private String email;

    private String name;

    @JsonProperty("email_confirmed")
    private Boolean emailConfirmed;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("phone_number_confirmed")
    private String phoneNumberConfirmed;

    public static CustomerJson mapFromCustomer(Customer customer) {

        return CustomerJson.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .name(customer.getName())
                .emailConfirmed(customer.getEmailConfirmed())
                .phoneNumber(customer.getPhoneNumber())
                .phoneNumberConfirmed(customer.getPhoneNumberConfirmed())
                .build();
    }
}
