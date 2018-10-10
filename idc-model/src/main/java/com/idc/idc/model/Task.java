package com.idc.idc.model;

import com.idc.idc.model.abstracts.AbstractAuditableEntity;
import com.idc.idc.model.enums.TaskStatus;
import com.idc.idc.model.users.Driver;
import lombok.*;

import javax.persistence.*;

@Table(name = "tasks")
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task extends AbstractAuditableEntity{
    public static final String DRIVER_ID = "driver_id";
    public static final String ORDER_ID = "order_id";
    public static final String STATUS = "task_status";

    @Column(name = STATUS)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = DRIVER_ID)
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = ORDER_ID)
    private Order order;
}
