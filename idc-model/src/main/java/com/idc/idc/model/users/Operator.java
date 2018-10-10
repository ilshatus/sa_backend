package com.idc.idc.model.users;

import com.idc.idc.model.abstracts.AbstractAuditableEntity;
import com.idc.idc.model.abstracts.AbstractEntity;
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
public class Operator extends AbstractAuditableEntity {
    public static final String EMAIL = "email";
    public static final String PASSWORD_HASH = "password_hash";
    public static final String NAME = "name";

    @Column(name = EMAIL, length = 100, unique = true)
    private String email;

    @Column(name = PASSWORD_HASH, length = 512)
    private String passwordHash;

    @Column(name = NAME, length = 250)
    private String name;
}
