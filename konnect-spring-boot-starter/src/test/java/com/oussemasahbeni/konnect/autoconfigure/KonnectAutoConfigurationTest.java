package com.oussemasahbeni.konnect.autoconfigure;


import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


class KonnectAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    KonnectAutoConfiguration.class,
                    RestClientAutoConfiguration.class,
                    JacksonAutoConfiguration.class))
            .withPropertyValues(
                    "konnect.api.key=test-api-key-from-properties",
                    "konnect.api.receiver-wallet-id=test-wallet-id-from-properties"
            );

    @Test
    void shouldContainKonnectClientBean() {
        contextRunner
                .run(context -> {
                    assertTrue(context.containsBean("konnectClient"));
                    assertTrue(context.containsBean("konnectRestClient"));
                    assertTrue(context.containsBean("konnectTemplate"));
                });
    }

    @Test
    void shouldContainDefaultBaseUrl() {
        contextRunner
                .run(context -> {
                    assertThat(context).hasSingleBean(KonnectProperties.class);
                    assertThat(context.getBean(KonnectProperties.class).baseUrl()).isEqualTo("https://api.sandbox.konnect.network/api/v2/");
                });
    }

    @Test
    void shouldSetCustomBaseUrl() {
        contextRunner
                .withPropertyValues("konnect.api.base-url=https://api.konnect.network/api/v2/")
                .run(context -> {
                    assertThat(context).hasSingleBean(KonnectProperties.class);
                    assertThat(context.getBean(KonnectProperties.class).baseUrl()).isEqualTo("https://api.konnect.network/api/v2/");
                });
    }

    @Test
    void shouldFailWithoutRequiredProperties() {
        contextRunner
                .withPropertyValues(
                        "konnect.api.key=",
                        "konnect.api.receiver-wallet-id="
                )
                .run(context -> assertThat(context).hasFailed());
    }
}