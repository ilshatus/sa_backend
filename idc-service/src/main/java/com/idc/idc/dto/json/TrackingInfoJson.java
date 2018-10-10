package com.idc.idc.dto.json;


import com.idc.idc.model.TrackingInformation;
import com.idc.idc.model.enums.UserState;
import com.idc.idc.model.users.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TrackingInfoJson {
    private String trackingCode;

    private String currentPosition;

    public static TrackingInfoJson mapFromTrackingInfo(TrackingInformation trackingInformation) {
        return TrackingInfoJson.builder().trackingCode(trackingInformation.getTracking_code())
                .currentPosition(trackingInformation.getCurrent_position()).build();
    }
}

