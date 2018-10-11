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
import net.sf.ehcache.search.parser.MCriteria;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderJson {

    private Long id;

    @JsonProperty("due_date")
    private LocalDateTime dueDate;

    private OrderOrigin origin;

    private OrderDestination destination;

    private OrderStatus status;

    private double weight;

    private double worth;

    private String description;

    private Customer customer;

    public static OrderJson mapFromOrder(Order Order) {

        return OrderJson.builder()
                .id(Order.getId())
                .dueDate(Order.getDueDate())
                .origin(Order.getOrigin())
                .destination(Order.getDestination())
                .status(Order.getStatus())
                .weight(Order.getWeight())
                .worth(Order.getWorth())
                .description(Order.getDescription())
                .customer(Order.getCustomer())
                .build();
    }
}
