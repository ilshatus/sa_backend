package com.idc.idc.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.Order;
import com.idc.idc.model.embeddable.OrderDestination;
import com.idc.idc.model.embeddable.OrderOrigin;
import com.idc.idc.model.enums.OrderStatus;
import com.idc.idc.model.users.Customer;
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

    @JsonProperty("due_date")
    private Date dueDate;

    private OrderOriginJson origin;

    private OrderDestinationJson destination;

    private OrderStatus status;

    private Double weight;

    private Long worth;

    private String description;

    private CustomerJson customer;

    @JsonProperty("deliver_price")
    private Long deliverPrice;

    public static OrderJson mapFromOrder(Order order) {
        Date date = null;
        if (order.getDueDate() != null) {
            date = Date.from(order.getDueDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return OrderJson.builder()
                .id(order.getId())
                .dueDate(date)
                .origin(OrderOriginJson.mapFromOrderOrigin(order.getOrigin()))
                .destination(OrderDestinationJson.mapFromOrderDestination(order.getDestination()))
                .status(order.getStatus())
                .weight(order.getWeight())
                .worth(order.getWorth())
                .description(order.getDescription())
                .customer(CustomerJson.mapFromCustomer(order.getCustomer()))
                .deliverPrice(order.getDeliverPrice())
                .build();
    }
}
