package com.idc.idc.model.users;

import com.idc.idc.model.abstracts.AbstractUserEntity;
import lombok.*;

import javax.persistence.*;

@Table(name = "customers", indexes = {
        @Index(name = "email_index", columnList = Customer.EMAIL)
})
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends AbstractUserEntity {
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String EMAIL_CONFIRMED = "email_confirmed";

    @Column(name = EMAIL, length = 100, unique = true)
    private String email;

    @Column(name = NAME, length = 250)
    private String name;

    @Column(name = EMAIL_CONFIRMED)
    private Boolean emailConfirmed;
}



