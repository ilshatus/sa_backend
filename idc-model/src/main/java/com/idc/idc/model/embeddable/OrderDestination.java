package com.idc.idc.model.embeddable;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDestination {
    public static final String TO_LATITUDE = "to_latitude";
    public static final String TO_LONGITUDE = "to_longitude";

    @Column(name = TO_LATITUDE)
    private Double fromLatitude;

    @Column(name = TO_LONGITUDE)
    private Double fromLongitude;
}
