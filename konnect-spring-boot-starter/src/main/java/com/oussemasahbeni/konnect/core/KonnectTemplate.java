package com.oussemasahbeni.konnect.core;


import com.oussemasahbeni.konnect.autoconfigure.KonnectProperties;
import com.oussemasahbeni.konnect.client.KonnectClient;
import com.oussemasahbeni.konnect.exception.InvalidPaymentReferenceException;
import com.oussemasahbeni.konnect.exception.KonnectApiException;
import com.oussemasahbeni.konnect.model.InitKonnectPaymentRequest;
import com.oussemasahbeni.konnect.model.InitKonnectPaymentResponse;
import com.oussemasahbeni.konnect.model.PaymentResponse;
import com.oussemasahbeni.konnect.utils.PaymentRefValidator;

import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * A high-level template for interacting with the Konnect API.
 * This class handles the merging of runtime data with configured defaults,
 * providing a simplified API for common operations.
 */
public class KonnectTemplate {


    private final KonnectClient konnectClient;
    private final KonnectProperties konnectProperties;

    public KonnectTemplate(KonnectClient konnectClient, KonnectProperties konnectProperties) {
        this.konnectClient = konnectClient;
        this.konnectProperties = konnectProperties;
    }

    /**
     * Initiates a payment with the minimum required information, using all configured defaults.
     *
     * @param amount The payment amount.
     * @return The response from the Konnect API.
     */
    public InitKonnectPaymentResponse initiatePayment(BigDecimal amount) {
        return initiatePayment(amount, null);
    }


    /**
     * Get payment details for a given payment reference.
     * This method fetches the payment details from the Konnect API using the provided payment reference.
     *
     * @param paymentRef The reference of the payment to retrieve details for.
     * @return PaymentResponse containing the details of the payment.
     * @throws KonnectApiException if the API call fails or returns an error status.
     */
    public PaymentResponse getPaymentDetails(String paymentRef) {

        if (!PaymentRefValidator.isValid(paymentRef)) {
            throw new InvalidPaymentReferenceException("Invalid payment reference format", paymentRef);
        }
        return konnectClient.getPaymentDetails(paymentRef);
    }

    /**
     * Initiates a payment, allowing for custom overrides of the default configuration.
     *
     * @param amount     The payment amount.
     * @param customizer A consumer function that receives a pre-configured builder to allow for overrides.
     * @return The response from the Konnect API.
     * <p>
     * Example:
     * template.initiatePayment(new BigDecimal("100"), builder -> builder.theme(KonnectTheme.LIGHT));
     */
    public InitKonnectPaymentResponse initiatePayment(BigDecimal amount, Consumer<InitKonnectPaymentRequest.Builder> customizer) {
        InitKonnectPaymentRequest.Builder builder = new InitKonnectPaymentRequest.Builder();

        builder.amount(amount);

        KonnectProperties.KonnectPaymentDefaults defaults = konnectProperties.defaults();
        builder.receiverWalletId(konnectProperties.receiverWalletId());
        builder.webhook(konnectProperties.webhookUrl());
        builder.token(defaults.konnectToken());
        builder.type(defaults.type());
        builder.lifespan(defaults.lifespan());
        builder.checkoutForm(defaults.checkoutForm());
        builder.acceptedPaymentMethods(defaults.acceptedKonnectPaymentMethods());
        builder.theme(defaults.konnectTheme());
        builder.addPaymentFeesToAmount(defaults.addPaymentFeesToAmount());

        if (customizer != null) {
            customizer.accept(builder);
        }

        InitKonnectPaymentRequest finalRequest = builder.build();
        return konnectClient.initiatePayment(finalRequest);
    }
}
