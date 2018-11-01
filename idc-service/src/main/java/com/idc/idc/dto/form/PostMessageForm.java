package com.idc.idc.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.Message;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostMessageForm {
    public static final String DRIVER_ID = "driver_id";
    public static final String OPERATOR_ID = "operator_id";
    public static final String TEXT = "text";
    public static final String IS_DRIVER_INITIATOR = "is_driver_initiator";

    @JsonProperty(value = DRIVER_ID)
    private Long driverId;

    @JsonProperty(value = OPERATOR_ID)
    private Long operatorId;

    @NotNull(message = "The text must not be null")
    @JsonProperty(value = TEXT)
    private String text;

    @JsonProperty(value = IS_DRIVER_INITIATOR)
    private Boolean isDriverInitiator;

    public Message toMessage() {
        return Message.builder()
                .text(text)
                .isDriverInitiator(isDriverInitiator)
                .isRead(false)
                .build();
    }
}
