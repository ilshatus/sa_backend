package com.idc.idc.model;

import com.idc.idc.model.abstracts.AbstractEntity;
import com.idc.idc.model.users.Driver;
import com.idc.idc.model.users.Operator;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Table(name = "messages")
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Message extends AbstractEntity {
    public static final String DRIVER_ID = "driver_id";
    public static final String OPERATOR_ID = "operator_id";
    public static final String POSTED_DATE = "posted_date";
    public static final String TEXT = "text";
    public static final String IS_DRIVER_INITIATOR = "is_driver_initiator";
    public static final String IS_READ = "is_read";

    @CreatedDate
    @Column(name = POSTED_DATE)
    private Date postedDate;

    @Column(name = TEXT)
    private String text;

    @ManyToOne
    @JoinColumn(name = DRIVER_ID)
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = OPERATOR_ID)
    private Operator operator;

    @Column(name = IS_DRIVER_INITIATOR)
    private Boolean isDriverInitiator;

    @Column(name = IS_READ)
    private Boolean isRead;
}
