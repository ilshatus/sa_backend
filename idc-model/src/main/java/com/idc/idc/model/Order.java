package com.idc.idc.model;

import com.idc.idc.model.abstracts.AbstractAuditableEntity;
import com.idc.idc.model.embeddable.OrderDestination;
import com.idc.idc.model.embeddable.OrderFrom;
import com.idc.idc.model.enums.OrderStatus;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "orders")
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends AbstractAuditableEntity {
    public static final String DUE_DATE = "due_date";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String STATUS = "status";
    public static final String WEIGHT = "weight";
    public static final String WORTH = "worth";
    public static final String DESCRIPTION = "description";
    public static final String CONTACTS = "contacts";
    public static final String TRACKING_CODE = "tracking_code";

    @Column(name = DUE_DATE)
    @Type(type = "java.time.LocalDateTime")
    private LocalDateTime due_date;

    @Embedded
    private OrderFrom orderFrom;

    @Embedded
    private OrderDestination orderDestination;

    @Column(name = STATUS)
    private OrderStatus status;

    @Column(name = WEIGHT)
    private double weight;

    @Column(name = WORTH)
    private double worth;

    @Column(name = DESCRIPTION)
    private String description;

    @Column(name = CONTACTS)
    private String contacts;

    @Column(name = TRACKING_CODE)
    private String tracking_code;
}
