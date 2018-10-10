package com.idc.idc.model.abstracts;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Setter
@Getter
@MappedSuperclass
public abstract class AbstractUserEntity extends AbstractAuditableEntity {
    public static final String PASSWORD_HASH = "password_hash";

    @Column(name = PASSWORD_HASH, length = 512)
    private String passwordHash;
}
