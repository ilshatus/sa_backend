package com.idc.idc.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.embeddable.OrderDestination;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderDestinationJson {

    @JsonProperty("from_latitude")
    private Double fromLatitude;

    @JsonProperty("from_longitude")
    private Double fromLongitude;

    public static OrderDestinationJson mapFromOrderDestination(OrderDestination OrderDestination) {

        return OrderDestinationJson.builder()
                .fromLatitude(OrderDestination.getFromLatitude())
                .fromLongitude(OrderDestination.getFromLongitude())
                .build();
    }
}
