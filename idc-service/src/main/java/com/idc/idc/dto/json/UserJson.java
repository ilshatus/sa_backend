package com.idc.idc.dto.json;

import com.idc.idc.model.users.Customer;
import com.idc.idc.model.enums.UserState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserJson {
    private Long id;

    private String email;

    private UserState state;

    public static UserJson mapFromUser(Customer customer) {
        return UserJson.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .state(customer.getState())
                .build();
    }
}
