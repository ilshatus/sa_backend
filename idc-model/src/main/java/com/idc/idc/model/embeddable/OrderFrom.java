package com.idc.idc.model.embeddable;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderFrom {
    public static final String FROM_LATITUDE = "from_latitude";
    public static final String FROM_LONGITUDE = "from_longitude";

    @Column(name = FROM_LATITUDE)
    private Double fromLatitude;

    @Column(name = FROM_LONGITUDE)
    private Double fromLongitude;
}
