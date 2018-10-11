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

    public static OrderOriginJson mapFromOrderOrigin(OrderOrigin OrderOrigin) {

        return OrderOriginJson.builder()
                .originLatitude(OrderOrigin.getOriginLatitude())
                .originLongitude(OrderOrigin.getOriginLongitude())
                .build();
    }
}
