package com.idc.idc.model;

import com.idc.idc.model.abstracts.AbstractAuditableEntity;
import com.idc.idc.model.embeddable.CurrentLocation;
import com.idc.idc.model.embeddable.OrderDestination;
import com.idc.idc.model.embeddable.OrderOrigin;
import com.idc.idc.model.enums.OrderStatus;
import com.idc.idc.model.users.Customer;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Table(name = "orders")
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends AbstractAuditableEntity {
    public static final String DUE_DATE = "due_date";
    public static final String STATUS = "status";
    public static final String WEIGHT = "weight";
    public static final String WORTH = "worth";
    public static final String DESCRIPTION = "description";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String DELIVERY_PRICE = "delivery_price";
    public static final String TRACKING_CODE = "tracking_code";

    @Column(name = DUE_DATE)
    private Date dueDate;

    @Embedded
    private OrderOrigin origin;

    @Embedded
    private OrderDestination destination;

    @Embedded
    private CurrentLocation location;

    @Column(name = STATUS)
    private OrderStatus status;

    @Column(name = WEIGHT)
    private Double weight;

    @Column(name = WORTH)
    private Long worth;

    @Column(name = DESCRIPTION)
    private String description;

    @Column(name = TRACKING_CODE)
    private String trackingCode;

    @ManyToOne
    @JoinColumn(name = CUSTOMER_ID)
    private Customer customer;

    @Column(name = DELIVERY_PRICE)
    private Long deliveryPrice;
}
