package com.idc.idc.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.idc.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MessageJson {
    private Long id;

    @JsonProperty("posted_date")
    private Long postedDate;

    @JsonProperty("driver")
    private SimpleDriverJson driver;

    @JsonProperty("operator")
    private SimpleOperatorJson operator;

    private String text;

    @JsonProperty("is_driver_initiator")
    private Boolean isDriverInitiator;

    public static MessageJson mapFromMessage(Message message) {
        return MessageJson.builder()
                .id(message.getId())
                .postedDate(message.getPostedDate().getTime())
                .driver(SimpleDriverJson.mapFromDriver(message.getDriver()))
                .operator(SimpleOperatorJson.mapFromOperator(message.getOperator()))
                .text(message.getText())
                .isDriverInitiator(message.getIsDriverInitiator())
                .build();
    }
}
