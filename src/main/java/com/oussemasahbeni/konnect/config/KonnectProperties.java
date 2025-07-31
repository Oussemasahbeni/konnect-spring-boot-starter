package com.oussemasahbeni.konnect.config;


import com.oussemasahbeni.konnect.enums.PaymentMethod;
import com.oussemasahbeni.konnect.enums.PaymentType;
import com.oussemasahbeni.konnect.enums.Theme;
import com.oussemasahbeni.konnect.enums.Token;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

/**
 * Configuration properties for Konnect payment defaults
 * Allows externalization of payment configuration with user override capabilities
 */
@ConfigurationProperties(prefix = "konnect")
public record KonnectProperties(
        @NotBlank(message = "Konnect API base URL cannot be blank")
        @URL(message = "Konnect API base URL must be a valid URL")
        @DefaultValue("https://api.sandbox.konnect.network/api/v2/")
        String baseUrl,

        @NotBlank(message = "Konnect API base URL must be configured")
        String key,

        KonnectPaymentDefaults defaults
) {

    public record KonnectPaymentDefaults(
            String receiverWalletId,
            Token token,
            PaymentType type,
            String description,
            List<PaymentMethod> acceptedPaymentMethods,
            Integer lifespan, // minutes
            Boolean checkoutForm,
            Theme theme,
            String webhookUrl
    ) {
        public KonnectPaymentDefaults {
            if (receiverWalletId == null) receiverWalletId = "6879130a6d5c4a3b95da6940";
            if (token == null) token = Token.TND;
            if (type == null) type = PaymentType.IMMEDIATE;
            if (lifespan == null) lifespan = 30;
            if (checkoutForm == null) checkoutForm = true;
        }
    }


}
