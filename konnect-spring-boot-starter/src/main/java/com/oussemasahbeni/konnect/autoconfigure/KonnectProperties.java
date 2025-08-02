package com.oussemasahbeni.konnect.autoconfigure;


import com.oussemasahbeni.konnect.model.enums.KonnectPaymentMethod;
import com.oussemasahbeni.konnect.model.enums.KonnectPaymentType;
import com.oussemasahbeni.konnect.model.enums.KonnectTheme;
import com.oussemasahbeni.konnect.model.enums.KonnectToken;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Configuration properties for Konnect payment defaults
 * Allows externalization of payment configuration with user override capabilities
 */
@ConfigurationProperties(prefix = "konnect.api")
@Validated
public record KonnectProperties(
        @NotBlank(message = "Konnect API base URL cannot be blank")
        @URL(message = "Konnect API base URL must be a valid URL")
        @DefaultValue("https://api.sandbox.konnect.network/api/v2/")
        String baseUrl,

        @NotBlank(message = "Konnect API key cannot be blank")
        String key,

        @NotBlank(message = "Konnect receiver wallet ID cannot be blank")
        String receiverWalletId,

        String webhookUrl,

        @DefaultValue
        KonnectPaymentDefaults defaults
) {

    public record KonnectPaymentDefaults(
            @DefaultValue("TND")
            KonnectToken konnectToken,
            @DefaultValue("IMMEDIATE")
            KonnectPaymentType type,
            List<KonnectPaymentMethod> acceptedKonnectPaymentMethods,
            @DefaultValue("30")
            Integer lifespan,
            @DefaultValue("true")
            Boolean checkoutForm,
            @DefaultValue("LIGHT")
            KonnectTheme konnectTheme,
            @DefaultValue("false")
            Boolean addPaymentFeesToAmount
    ) {
    }

}
