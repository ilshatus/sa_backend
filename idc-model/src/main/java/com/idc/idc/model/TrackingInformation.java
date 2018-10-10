package com.idc.idc.model;

import com.idc.idc.model.abstracts.AbstractAuditableEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "orders")
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingInformation extends AbstractAuditableEntity {
    public static final String TRACKING_CODE = "tracking_code";
    public static final String CURRENT_POSITION = "current_position";

    @Column(name = TRACKING_CODE)
    private String tracking_code;

    @Column(name = CURRENT_POSITION)
    private String current_position;
}
