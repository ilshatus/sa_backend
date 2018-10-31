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
    public static final String ORIGIN_FULL_ADDRESS = "origin_full_address";
    public static final String ORIGIN_SHORT_ADDRESS = "origin_short_address";

    @Column(name = ORIGIN_LATITUDE)
    private Double originLatitude;

    @Column(name = ORIGIN_LONGITUDE)
    private Double originLongitude;

    @Column(name = ORIGIN_FULL_ADDRESS)
    private String originFullAddress;

    @Column(name = ORIGIN_SHORT_ADDRESS)
    private String originShortAddress;
}
