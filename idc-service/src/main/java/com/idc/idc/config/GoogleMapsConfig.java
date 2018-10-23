package com.idc.idc.config;

import com.google.maps.GaeRequestHandler;
import com.google.maps.GeoApiContext;
import com.idc.idc.settings.GoogleMapsSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleMapsConfig {

    @Autowired
    private GoogleMapsSettings googleMapsSettings;

    /*@Bean
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder(new GaeRequestHandler.Builder())
                .apiKey(googleMapsSettings.getApiKey())
                .build();
    }*/
}
