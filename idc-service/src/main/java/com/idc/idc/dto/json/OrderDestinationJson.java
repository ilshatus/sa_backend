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

    @JsonProperty("destination_latitude")
    private Double destinationLatitude;

    @JsonProperty("destination_longitude")
    private Double destinationLongitude;

    public static OrderDestinationJson mapFromOrderDestination(OrderDestination OrderDestination) {
        return OrderDestinationJson.builder()
                .destinationLatitude(OrderDestination.getDestinationLatitude())
                .destinationLongitude(OrderDestination.getDestinationLongitude())
                .build();
    }
}
