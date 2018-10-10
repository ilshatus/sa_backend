package com.idc.idc.repository;

import com.idc.idc.model.TrackingInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackingInformationRepository extends JpaRepository<TrackingInformation, String> {
    Optional<TrackingInformation> findTrackingInformationByTracking_codeAnd(String trackingCode);


}
