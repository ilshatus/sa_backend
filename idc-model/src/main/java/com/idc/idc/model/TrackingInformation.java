package com.idc.idc.model;

import com.idc.idc.model.abstracts.AbstractAuditableEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "tracking_codes")
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingInformation extends AbstractAuditableEntity {
    public static final String TRACKING_CODE = "trackingCode";
    public static final String CURRENT_POSITION = "currentPosition";

    @Column(name = TRACKING_CODE)
    private String trackingCode;

    @Column(name = CURRENT_POSITION)
    private String currentPosition;
}
