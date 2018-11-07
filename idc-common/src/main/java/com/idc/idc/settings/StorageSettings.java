package com.idc.idc.settings;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "idc.storage")
@Validated
public class StorageSettings {

    @NotNull(message = "Provider name not specified")
    private String provider;

    @NotNull(message = "Identity (Access Key ID / Client ID) not specified")
    private String identity;

    @NotNull(message = "Credential (Secret Access Key / Private Key) not specified")
    private String credential;

    @NotNull(message = "Container / Bucket name not specified")
    private String container;
}
