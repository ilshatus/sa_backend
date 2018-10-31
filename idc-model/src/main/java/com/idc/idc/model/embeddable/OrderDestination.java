package com.idc.idc.model.embeddable;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDestination {
    public static final String DESTINATION_LATITUDE = "destination_latitude";
    public static final String DESTINATION_LONGITUDE = "destination_longitude";
    public static final String DESTINATION_FULL_ADDRESS = "destination_full_address";
    public static final String DESTINATION_SHORT_ADDRESS = "destination_short_address";

    @Column(name = DESTINATION_LATITUDE)
    private Double destinationLatitude;

    @Column(name = DESTINATION_LONGITUDE)
    private Double destinationLongitude;

    @Column(name = DESTINATION_FULL_ADDRESS)
    private String destinationFullAddress;

    @Column(name = DESTINATION_SHORT_ADDRESS)
    private String destinationShortAddress;
}
