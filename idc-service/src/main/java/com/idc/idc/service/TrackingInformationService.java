package com.idc.idc.service;

import com.idc.idc.model.TrackingInformation;
import com.idc.idc.repository.TrackingInformationRepository;

public interface TrackingInformationService {
    TrackingInformation getTrackingInformation(String trackingCode);

    TrackingInformation updateTrackingInformation(TrackingInformation trackingInformation);

    void submitTrackingInformation(TrackingInformation trackingInformation);

    void registerTrackingInformation(String trackingCode, String currentPosition);

    void updateTrackingInformation(String trackingCode, String currentPosition);

}
