package io.github.oussemasahbeni.konnect.autoconfigure;


import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.oussemasahbeni.konnect.client.KonnectClient;
import io.github.oussemasahbeni.konnect.core.KonnectTemplate;
import io.github.oussemasahbeni.konnect.core.KonnectWebhookHandler;

/**
 * Spring Boot auto-configuration for the Konnect Payment Gateway integration.
 * This configuration class automatically sets up all the necessary beans for
 * interacting with the Konnect API when the required properties are present.
 * 
 * <p>The auto-configuration will create the following beans:
 * <ul>
 *   <li>{@link RestClient} - Pre-configured HTTP client with authentication and base URL</li>
 *   <li>{@link KonnectClient} - Low-level API client for making HTTP requests</li>
 *   <li>{@link KonnectTemplate} - High-level template for common operations</li>
 *   <li>{@link KonnectWebhookHandler} - Handler for processing incoming webhooks</li>
 * </ul>
 * 
 * <p>This configuration is activated when the property {@code konnect.api.key} is present.
 * All beans are created with {@code @ConditionalOnMissingBean} to allow for custom implementations.
 * 
 * @see KonnectProperties
 * @see KonnectTemplate
 * @see KonnectClient
 */
@AutoConfiguration
@EnableConfigurationProperties(KonnectProperties.class)
@ConditionalOnProperty(prefix = "konnect.api", name = "key")
public class KonnectAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(KonnectAutoConfiguration.class);

    private final KonnectProperties properties;

    /**
     * Constructs the auto-configuration with the specified properties.
     * 
     * @param properties the Konnect configuration properties
     */
    public KonnectAutoConfiguration(KonnectProperties properties) {
        this.properties = properties;
    }

    /**
     * Creates a pre-configured RestClient for making requests to the Konnect API.
     * The client is configured with:
     * <ul>
     *   <li>Base URL pointing to the Konnect API</li>
     *   <li>Authentication header with the API key</li>
     *   <li>Default content type set to application/json</li>
     *   <li>Connection and read timeouts</li>
     * </ul>
     * 
     * @return a configured RestClient for Konnect API communication
     */
    @Bean(name = "konnectRestClient")
    @ConditionalOnMissingBean(name = "konnectRestClient")
    RestClient konnectRestClient() {
        log.info("Initializing Konnect RestClient with base URL: {}", properties.baseUrl());

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(5)); // 5 second connect timeout
        requestFactory.setReadTimeout(Duration.ofSeconds(30)); // 30 second read timeout

        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .requestFactory(requestFactory)
                .defaultHeader("x-api-key", properties.key())
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Creates the KonnectClient bean for low-level API communication.
     * This client handles HTTP requests, error handling, and JSON serialization.
     * 
     * @param konnectRestClient the pre-configured RestClient
     * @param objectMapper the ObjectMapper for JSON processing
     * @return a KonnectClient instance
     */
    @Bean
    @ConditionalOnMissingBean
    public KonnectClient konnectClient(RestClient konnectRestClient, ObjectMapper objectMapper) {
        log.info("Initializing KonnectClient implementation.");
        return new KonnectClient(konnectRestClient, objectMapper);
    }

    /**
     * Creates the KonnectWebhookHandler bean for processing incoming webhooks.
     * This handler provides secure webhook processing following Konnect best practices.
     * 
     * @param konnectTemplate the KonnectTemplate for making API calls
     * @return a KonnectWebhookHandler instance
     */
    @Bean
    @ConditionalOnMissingBean
    public KonnectWebhookHandler konnectWebhookHandler(KonnectTemplate konnectTemplate) {
        return new KonnectWebhookHandler(konnectTemplate);
    }

    /**
     * Creates the KonnectTemplate bean for high-level API operations.
     * This template provides a simplified interface for common payment operations
     * and automatically applies configured defaults.
     * 
     * @param konnectClient the low-level HTTP client
     * @param konnectProperties the configuration properties with defaults
     * @return a KonnectTemplate instance
     */
    @Bean
    @ConditionalOnMissingBean
    public KonnectTemplate konnectTemplate(KonnectClient konnectClient, KonnectProperties konnectProperties) {
        log.info("Initializing KonnectTemplate with configured defaults.");
        return new KonnectTemplate(konnectClient, konnectProperties);
    }
}
