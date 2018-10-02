package com.idc.idc.model;

import com.idc.idc.model.abstracts.AbstractAuditableEntity;
import com.idc.idc.model.enums.UserRole;
import com.idc.idc.model.enums.UserState;
import lombok.*;

import javax.persistence.*;


@Table(name = "users", indexes = {
        @Index(name = "email_index", columnList = User.EMAIL)
})
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractAuditableEntity {
    public static final String EMAIL = "email";
    public static final String PASSWORD_HASH = "password_hash";
    public static final String NAME = "name";
    
    @Column(name = EMAIL, length = 100, unique = true)
    private String email;

    @Column(name = PASSWORD_HASH, length = 512)
    private String passwordHash;

    @Column(name = NAME, length = 250)
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column
    @Enumerated(EnumType.STRING)
    private UserState state;
}



