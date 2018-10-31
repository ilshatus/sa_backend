package com.idc.idc.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.Order;
import com.idc.idc.model.embeddable.OrderDestination;
import com.idc.idc.model.embeddable.OrderOrigin;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
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
    public static final String ORIGIN_FULL_ADDRESS = "origin_full_address";
    public static final String DESTINATION_FULL_ADDRESS = "destination_full_address";
    public static final String ORIGIN_SHORT_ADDRESS = "origin_short_address";
    public static final String DESTINATION_SHORT_ADDRESS = "destination_short_address";

    @NotNull(message = "The weight must not be null")
    @JsonProperty(value = WEIGHT, required = true)
    private Double weight;

    @NotNull(message = "The worth must not be null")
    @JsonProperty(value = WORTH, required = true)
    private Long worth;

    @NotBlank(message = "The description must not be empty")
    @JsonProperty(value = DESCRIPTION, required = true)
    private String description;

    @NotNull(message = "The due date must not be null")
    @JsonProperty(value = DUE_DATE, required = true)
    private Long dueDate;

    @NotNull(message = "The origin lat must not be null")
    @JsonProperty(value = ORIGIN_LAT, required = true)
    private Double originLat;

    @NotNull(message = "The destination lat must not be null")
    @JsonProperty(value = DESTINATION_LAT, required = true)
    private Double destinationLat;

    @NotNull(message = "The origin long must not be null")
    @JsonProperty(value = ORIGIN_LONG, required = true)
    private Double originLong;

    @NotNull(message = "The destination long must not be null")
    @JsonProperty(value = DESTINATION_LONG, required = true)
    private Double destinationLong;

    @NotBlank(message = "The origin full address must not be empty")
    @JsonProperty(value = ORIGIN_FULL_ADDRESS, required = true)
    private String originFullAddress;

    @NotBlank(message = "The destination full address must not be empty")
    @JsonProperty(value = DESTINATION_FULL_ADDRESS, required = true)
    private String destinationFullAddress;

    @NotBlank(message = "The origin short address must not be empty")
    @JsonProperty(value = ORIGIN_SHORT_ADDRESS, required = true)
    private String originShortAddress;

    @NotBlank(message = "The destination short address must not be empty")
    @JsonProperty(value = DESTINATION_SHORT_ADDRESS, required = true)
    private String destinationShortAddress;

    public Order toOrder() {
        return Order.
                builder()
                .weight(weight)
                .worth(worth)
                .description(description)
                .dueDate(new Date(dueDate))
                .origin(new OrderOrigin(originLat, originLong,
                        originFullAddress, originShortAddress))
                .destination(new OrderDestination(destinationLat, destinationLong,
                        destinationFullAddress, destinationShortAddress))
                .build();
    }

}
