package com.idc.idc.model.abstracts;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableEntity extends AbstractEntity {
    public static final String CREATED_DATE_COLUMN_NAME = "created_date";
    public static final String LAST_MODIFIED_DATE_COLUMN_NAME = "last_modified_date";

    @CreatedDate
    @Column(name = CREATED_DATE_COLUMN_NAME)
    private Date createdDate;

    @LastModifiedDate
    @Column(name = LAST_MODIFIED_DATE_COLUMN_NAME)
    private Date lastModifiedDate;
}
