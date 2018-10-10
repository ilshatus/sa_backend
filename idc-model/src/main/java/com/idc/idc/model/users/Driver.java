package com.idc.idc.model.users;

import com.idc.idc.model.abstracts.AbstractUserEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Table(name = "drivers", indexes = {
        @Index(name = "email_index", columnList = Customer.EMAIL)
})
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends AbstractUserEntity {
    public static final String EMAIL = "email";
    public static final String NAME = "name";

    @Column(name = EMAIL, length = 100, unique = true)
    private String email;

    @Column(name = NAME, length = 250)
    private String name;
}
