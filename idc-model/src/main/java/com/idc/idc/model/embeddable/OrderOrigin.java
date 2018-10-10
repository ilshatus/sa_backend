package com.idc.idc.model.embeddable;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderOrigin {
    public static final String ORIGIN_LATITUDE = "origin_latitude";
    public static final String ORIGIN_LONGITUDE = "origin_longitude";

    @Column(name = ORIGIN_LATITUDE)
    private Double originLatitude;

    @Column(name = ORIGIN_LONGITUDE)
    private Double originLongitude;
}
