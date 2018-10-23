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

    @JsonProperty("destination_full_address")
    private String destinationFullAddress;

    @JsonProperty("destination_short_address")
    private String destinationShortAddress;

    public static OrderDestinationJson mapFromOrderDestination(OrderDestination orderDestination) {
        return OrderDestinationJson.builder()
                .destinationLatitude(orderDestination.getDestinationLatitude())
                .destinationLongitude(orderDestination.getDestinationLongitude())
                .destinationFullAddress(orderDestination.getDestinationFullAddress())
                .destinationShortAddress(orderDestination.getDestinationShortAddress())
                .build();
    }
}
