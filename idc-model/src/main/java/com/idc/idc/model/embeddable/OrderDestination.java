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

    @Column(name = DESTINATION_LATITUDE)
    private Double destinationLatitude;

    @Column(name = DESTINATION_LONGITUDE)
    private Double destinationLongitude;
}
