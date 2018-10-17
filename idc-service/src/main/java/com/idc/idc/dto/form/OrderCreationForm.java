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
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
public class OrderCreationForm {
    public static final String WEIGHT = "weight";
    public static final String WORTH = "worth";
    public static final String DESCRIPTION = "description";
    public static final String DUE_DATE = "dueDate";
    public static final String ORIGIN_LAT = "origin_lat";
    public static final String DESTINATION_LAT = "destination_lat";
    public static final String ORIGIN_LONG = "origin_long";
    public static final String DESTINATION_LONG = "destination_long";

    @ApiModelProperty(value = WEIGHT, required = true)
    private Double weight;

    @ApiModelProperty(value = WORTH, required = true)
    private Long worth;

    @ApiModelProperty(value = DESCRIPTION, required = true)
    private String description;

    @ApiModelProperty(value = DUE_DATE, required = true)
    private Date dueDate;

    @ApiModelProperty(value = ORIGIN_LAT, required = true)
    private Double originLat;

    @ApiModelProperty(value = DESTINATION_LAT, required = true)
    private Double destinationLat;

    @ApiModelProperty(value = ORIGIN_LONG, required = true)
    private Double originLong;

    @ApiModelProperty(value = DESTINATION_LONG, required = true)
    private Double destinationLong;

    public Order toOrder() {
        return Order.
                builder()
                .weight(weight)
                .worth(worth)
                .description(description)
                .dueDate(dueDate != null ?
                        dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null)
                .origin(new OrderOrigin(originLat, originLong))
                .destination(new OrderDestination(destinationLat, destinationLong))
                .build();
    }

}
