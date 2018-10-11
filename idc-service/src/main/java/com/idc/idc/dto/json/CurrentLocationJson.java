package com.idc.idc.dto.json;

import com.idc.idc.model.embeddable.CurrentLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CurrentLocationJson {


    private Double latitude;

    private Double longitude;

    public static CurrentLocationJson mapFromCurrentLocation(CurrentLocation CurrentLocation) {

        return CurrentLocationJson.builder()
                .latitude(CurrentLocation.getLatitude())
                .longitude(CurrentLocation.getLongitude())
                .build();
    }
}
