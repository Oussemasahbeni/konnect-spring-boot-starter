package com.oussemasahbeni.konnect.core;

import com.oussemasahbeni.konnect.model.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A service class dedicated to processing incoming webhooks from Konnect.
 * It follows the best practice of using the webhook's payment reference
 * to make a secure, authenticated API call to fetch the authoritative payment status.
 */
public class KonnectWebhookHandler {

    private static final Logger log = LoggerFactory.getLogger(KonnectWebhookHandler.class);

    private final KonnectTemplate konnectTemplate;

    /**
     * Constructs a webhook handler.
     *
     * @param konnectTemplate The configured KonnectTemplate to make API calls.
     */
    public KonnectWebhookHandler(KonnectTemplate konnectTemplate) {
        this.konnectTemplate = konnectTemplate;
    }

    /**
     * Securely processes an incoming webhook.
     * <p>
     * This method takes the untrusted payment reference from a webhook, validates its
     * format, and uses it to fetch the full, trusted payment details from the Konnect API.
     *
     * @param paymentRef The payment_ref received from the webhook query parameter.
     * @return The full, verified PaymentResponse object.
     * @throws com.oussemasahbeni.konnect.exception.KonnectApiException              if fetching details fails (e.g., 404 Not Found).
     * @throws com.oussemasahbeni.konnect.exception.InvalidPaymentReferenceException if the ref has an invalid format.
     */
    public PaymentResponse processWebhook(String paymentRef) {
        log.info("Processing incoming Konnect webhook for payment_ref: {}", paymentRef);
        PaymentResponse paymentDetails = konnectTemplate.getPaymentDetails(paymentRef);
        log.info("Successfully verified webhook. Payment '{}' has status: {}", paymentRef, paymentDetails.payment().status());
        return paymentDetails;
    }
}