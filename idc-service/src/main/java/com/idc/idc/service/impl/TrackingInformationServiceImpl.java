package com.idc.idc.service.impl;

import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.TrackingInformation;
import com.idc.idc.repository.TrackingInformationRepository;
import com.idc.idc.service.TrackingInformationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@Transactional
public class TrackingInformationServiceImpl implements TrackingInformationService {
    private TrackingInformationRepository trackingInformationRepository;

    @Autowired
    public TrackingInformationServiceImpl(TrackingInformationRepository trackingInformationRepository) {
        this.trackingInformationRepository = trackingInformationRepository;
    }

    @Override
    public void registerTrackingInformation(String trackingCode, String currentPosition) {
        TrackingInformation trackingInformation = TrackingInformation.builder()
                .tracking_code(trackingCode).current_position(currentPosition).build();
    }

    @Override
    public void updateTrackingInformation(String trackingCode, String currentPosition) {
        TrackingInformation trackingInformation = getTrackingInformation(trackingCode);
        if (StringUtils.isBlank(currentPosition)) {
            return;
        }
        trackingInformation.setCurrent_position(currentPosition);
        submitTrackingInformation(trackingInformation);
    }

    @Override
    public void submitTrackingInformation(TrackingInformation trackingInformation) {
        trackingInformationRepository.save(trackingInformation);
    }

    @Override
    public TrackingInformation getTrackingInformation(String trackingCode) {
        if (StringUtils.isBlank(trackingCode)) {
            return null;
        }
        Optional<TrackingInformation> oneByTrackingCode = trackingInformationRepository.
                findTrackingInformationByTracking_codeAnd(trackingCode);
        return oneByTrackingCode.orElseThrow(() -> new NotFoundException(
                String.format("Order with %s tracking code not found", trackingCode)));
    }

    @Override
    public TrackingInformation updateTrackingInformation(TrackingInformation trackingInformation) {
        return null;
    }


}
