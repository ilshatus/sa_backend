package com.idc.idc.model;

import com.idc.idc.model.abstracts.AbstractAuditableEntity;
import com.idc.idc.model.embeddable.OrderDestination;
import com.idc.idc.model.embeddable.OrderOrigin;
import com.idc.idc.model.enums.OrderStatus;
import com.idc.idc.model.users.Customer;
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
    public static final String DUE_DATE = "dueDate";
    public static final String STATUS = "status";
    public static final String WEIGHT = "weight";
    public static final String WORTH = "worth";
    public static final String DESCRIPTION = "description";
    public static final String CONTACTS = "contacts";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String DELIVER_PRICE = "deliver_price";

    @Column(name = DUE_DATE)
    @Type(type = "java.time.LocalDateTime")
    private LocalDateTime dueDate;

    @Embedded
    private OrderOrigin origin;

    @Embedded
    private OrderDestination destination;

    @Column(name = STATUS)
    private OrderStatus status;

    @Column(name = WEIGHT)
    private double weight;

    @Column(name = WORTH)
    private long worth;

    @Column(name = DESCRIPTION)
    private String description;

    @ManyToOne
    @JoinColumn(name = CUSTOMER_ID)
    private Customer customer;

    @Column(name = DELIVER_PRICE)
    private long deliverPrice;
}
