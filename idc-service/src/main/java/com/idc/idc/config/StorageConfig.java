package com.idc.idc.config;

import com.idc.idc.settings.StorageSettings;
import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class StorageConfig {

    @Autowired
    private StorageSettings storageSettings;

    @Lazy
    @Bean
    public BlobStore uploadBlobStore() {
        return ContextBuilder
                .newBuilder(storageSettings.getProvider())
                .credentials(storageSettings.getIdentity(), storageSettings.getCredential())
                .buildView(BlobStoreContext.class)
                .getBlobStore();
    }
}
