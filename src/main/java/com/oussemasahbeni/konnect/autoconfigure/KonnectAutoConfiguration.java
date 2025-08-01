package com.oussemasahbeni.konnect.autoconfigure;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.oussemasahbeni.konnect.client.KonnectClient;
import com.oussemasahbeni.konnect.client.KonnectClientImpl;
import com.oussemasahbeni.konnect.core.KonnectTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AutoConfiguration
@EnableConfigurationProperties(KonnectProperties.class)
public class KonnectAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(KonnectAutoConfiguration.class);

    private final KonnectProperties konnectProperties;

    public KonnectAutoConfiguration(KonnectProperties konnectProperties) {
        this.konnectProperties = konnectProperties;
    }

    @Bean(name = "konnectRestClient")
    @ConditionalOnMissingBean(name = "konnectRestClient")
    RestClient konnectRestClient(RestClient.Builder builder) {
        log.info("Initializing Konnect RestClient with base URL: {}", konnectProperties.baseUrl());

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(5)); // 5 second connect timeout
        requestFactory.setReadTimeout(Duration.ofSeconds(30)); // 30 second read timeout
        return builder
                .baseUrl(konnectProperties.baseUrl())
                .requestFactory(requestFactory)
                .defaultHeader("x-api-key", konnectProperties.key())
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    KonnectClient konnectClient(RestClient konnectRestClient, ObjectMapper objectMapper) {
        log.info("Initializing KonnectClient implementation.");
        return new KonnectClientImpl(konnectRestClient, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public KonnectTemplate konnectTemplate(KonnectClient konnectClient, KonnectProperties konnectProperties) {
        log.info("Initializing KonnectTemplate with configured defaults.");
        return new KonnectTemplate(konnectClient, konnectProperties);
    }
}
