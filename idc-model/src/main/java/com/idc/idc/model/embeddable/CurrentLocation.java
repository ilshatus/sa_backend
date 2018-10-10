package com.idc.idc.model.embeddable;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentLocation {
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    @Column(name = LATITUDE)
    private Double latitude;

    @Column(name = LONGITUDE)
    private Double longitude;
}
