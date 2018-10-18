package com.idc.idc.dto.json;


import com.idc.idc.model.TrackingInformation;
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
        return TrackingInfoJson.builder().trackingCode(trackingInformation.getTrackingCode())
                .currentPosition(trackingInformation.getCurrentPosition()).build();
    }
}

