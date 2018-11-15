package com.idc.idc.dto.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.Order;
import com.idc.idc.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderJson {

    private Long id;

    @JsonProperty("creation_date")
    private Long creationDate;

    @JsonProperty("due_date")
    private Long dueDate;

    private OrderOriginJson origin;

    private OrderDestinationJson destination;

    private OrderStatus status;

    private Double weight;

    private Long worth;

    private String description;

    private CustomerJson customer;

    private CurrentLocationJson location;

    @JsonProperty("deliver_price")
    private Long deliverPrice;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("task")
    private SimpleTaskJson simpleTaskJson;

    public static OrderJson mapFromOrder(Order order) {
        return OrderJson.builder()
                .id(order.getId())
                .creationDate(order.getCreatedDate().getTime())
                .dueDate(order.getDueDate().getTime())
                .origin(OrderOriginJson.mapFromOrderOrigin(order.getOrigin()))
                .destination(OrderDestinationJson.mapFromOrderDestination(order.getDestination()))
                .status(order.getStatus())
                .weight(order.getWeight())
                .worth(order.getWorth())
                .description(order.getDescription())
                .customer(CustomerJson.mapFromCustomer(order.getCustomer()))
                .location(CurrentLocationJson.mapFromCurrentLocation(order.getLocation()))
                .deliverPrice(order.getDeliveryPrice())
                .build();
    }
}
