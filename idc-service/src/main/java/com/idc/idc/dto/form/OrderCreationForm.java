package com.idc.idc.dto.form;

import com.idc.idc.model.Order;
import com.idc.idc.model.embeddable.OrderDestination;
import com.idc.idc.model.embeddable.OrderOrigin;
import com.idc.idc.model.users.Customer;
import com.idc.idc.model.users.Operator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderCreationForm {
    public static final String WEIGHT = "weight";
    public static final String WORTH = "worth";
    public static final String DESCRIPTION = "description";
    public static final String DUE_DATE = "dueDate";
    public static final String ORIGIN = "origin";
    public static final String DESTINATION = "destionation";

    @ApiModelProperty(value = WEIGHT, required = true)
    private Double weight;

    @ApiModelProperty(value = WORTH, required = true)
    private Double worth;

    @ApiModelProperty(value = DESCRIPTION, required = true)
    private String description;

    @ApiModelProperty(value = DUE_DATE, required = true)
    private LocalDateTime dueDate;

    @ApiModelProperty(value = ORIGIN, required = true)
    private OrderOrigin orderOrigin;

    @ApiModelProperty(value = DESTINATION, required = true)
    private OrderDestination orderDestination;

    public Order toOrder(Customer customer) {
        return Order.
                builder()
                .customer(customer)
                .weight(weight)
                .worth(worth)
                .description(description)
                .dueDate(dueDate)
                .origin(orderOrigin)
                .destination(orderDestination)
                .build();

    }

}
