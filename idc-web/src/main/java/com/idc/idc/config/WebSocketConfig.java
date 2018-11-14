package com.idc.idc.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.idc.idc.settings.WebSocketSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
public class WebSocketConfig {
    private WebSocketSettings webSocketSettings;
    private ResourceLoader resourceLoader;

    @Autowired
    public WebSocketConfig(WebSocketSettings webSocketSettings,
                           ResourceLoader resourceLoader) {
        this.webSocketSettings = webSocketSettings;
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public SocketIOServer socketIOServer() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:keystore.p12");
        com.corundumstudio.socketio.Configuration config =
                new com.corundumstudio.socketio.Configuration();
        config.setHostname(webSocketSettings.getHost());
        config.setPort(webSocketSettings.getPort());
        config.setKeyStorePassword("pizdablya");
        config.setKeyStoreFormat("PKCS12");
        config.setKeyStore(resource.getInputStream());
        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer ssrv) {
        return new SpringAnnotationScanner(ssrv);
    }
}
