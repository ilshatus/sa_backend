package com.idc.idc.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.embeddable.OrderOrigin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderOriginJson {

    @JsonProperty("origin_latitude")
    private Double originLatitude;

    @JsonProperty("origin_longitude")
    private Double originLongitude;

    @JsonProperty("origin_full_address")
    private String originFullAddress;

    @JsonProperty("origin_short_address")
    private String originShortAddress;

    public static OrderOriginJson mapFromOrderOrigin(OrderOrigin orderOrigin) {
        return OrderOriginJson.builder()
                .originLatitude(orderOrigin.getOriginLatitude())
                .originLongitude(orderOrigin.getOriginLongitude())
                .originFullAddress(orderOrigin.getOriginFullAddress())
                .originShortAddress(orderOrigin.getOriginShortAddress())
                .build();
    }
}
