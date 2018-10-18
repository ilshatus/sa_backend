package com.idc.idc.model;

import com.idc.idc.model.abstracts.AbstractAuditableEntity;
import com.idc.idc.model.embeddable.CurrentLocation;
import com.idc.idc.model.enums.VehicleType;
import com.idc.idc.model.users.Driver;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Table(name = "vehicles")
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends AbstractAuditableEntity {
    private static final String VEHICLE_TYPE = "vehicle_type";
    private static final String ID_NUMBER = "id_number";
    private static final String MODEL = "model";

    @Column(name = VEHICLE_TYPE)
    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @Column(name = ID_NUMBER)
    private String idNumber;

    @Column(name = MODEL)
    private String model;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Driver> drivers;

    @Embedded
    private CurrentLocation location;
}
