package com.idc.idc.model.users;

import com.idc.idc.model.abstracts.AbstractUserEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Table(name = "operators", indexes = {
        @Index(name = "email_index", columnList = Customer.EMAIL)
})
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Operator extends AbstractUserEntity {
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String ADMIN = "admin";

    @Column(name = EMAIL, length = 100, unique = true)
    private String email;

    @Column(name = NAME, length = 250)
    private String name;

    @Column(name = ADMIN)
    private Boolean admin;
}
