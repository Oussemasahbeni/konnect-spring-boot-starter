package com.oussemasahbeni.konnect.client;

import com.oussemasahbeni.konnect.config.KonnectConfiguration;
import com.oussemasahbeni.konnect.config.KonnectProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KonnectClientImplTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    KonnectConfiguration.class,
                    RestClientAutoConfiguration.class,
                    JacksonAutoConfiguration.class));

    @Test
    void shouldContainKonnectClientBean() {
        contextRunner.run(context -> {
            assertTrue(context.containsBean("konnectClient"));
            assertInstanceOf(KonnectClientImpl.class, context.getBean(KonnectClient.class));
            assertTrue(context.containsBean("konnectRestClient"));
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
                .withPropertyValues("konnect.baseUrl=https://api.konnect.network/api/v2/")
                .run(context -> {
                    assertThat(context).hasSingleBean(KonnectProperties.class);
                    assertThat(context.getBean(KonnectProperties.class).baseUrl()).isEqualTo("https://api.konnect.network/api/v2/");
                });
    }


    @Test
    void initiatePayment() {
    }

    @Test
    void getPaymentDetails() {
    }
}