package com.idc.idc.dto.json;

import com.idc.idc.model.User;
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

    public static UserJson mapFromUser(User user) {
        return UserJson.builder()
                .id(user.getId())
                .email(user.getEmail())
                .state(user.getState())
                .build();
    }
}
