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
public class Task extends AbstractAuditableEntity {
    private static final String ORDER_ID = "order_id";
    private static final String DRIVER_ID = "driver_id";
    private static final String TASK_STATUS = "task_status";

    @ManyToOne
    @JoinColumn(name = ORDER_ID)
    private Order order;

    @ManyToOne
    @JoinColumn(name = ORDER_ID)
    private Driver driver;

    @Column(name = TASK_STATUS)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
}
