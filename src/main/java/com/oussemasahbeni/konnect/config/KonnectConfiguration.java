package com.oussemasahbeni.konnect.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.oussemasahbeni.konnect.client.KonnectClient;
import com.oussemasahbeni.konnect.client.KonnectClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@EnableConfigurationProperties(KonnectProperties.class)
public class KonnectConfiguration {

    private static final Logger log = LoggerFactory.getLogger(KonnectConfiguration.class);

    private final KonnectProperties konnectProperties;

    public KonnectConfiguration(KonnectProperties konnectProperties) {
        this.konnectProperties = konnectProperties;
    }

    @Bean(name = "konnectRestClient")
    public RestClient konnectRestClient(RestClient.Builder builder) {
        log.info("Initializing Konnect RestClient with base URL: {}", konnectProperties.baseUrl());
        return builder
                .baseUrl(konnectProperties.baseUrl())
                .defaultHeader("x-api-key", konnectProperties.key())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean
    KonnectClient konnectClient(RestClient restClient, ObjectMapper objectMapper) {
        return new KonnectClientImpl(restClient, objectMapper) {
        };
    }
}
