package com.oussema.konnect.service;


import com.oussemasahbeni.konnect.core.KonnectPaymentVerifier;
import com.oussemasahbeni.konnect.core.KonnectWebhookHandler;
import com.oussemasahbeni.konnect.exception.InvalidPaymentReferenceException;
import com.oussemasahbeni.konnect.exception.WebhookProcessingException;
import com.oussemasahbeni.konnect.model.PaymentResponse;
import com.oussemasahbeni.konnect.model.enums.KonnectToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class WebhookService {

    private final KonnectWebhookHandler konnectWebhookHandler;

    @Autowired
    public WebhookService(KonnectWebhookHandler konnectWebhookHandler) {
        this.konnectWebhookHandler = konnectWebhookHandler;
    }

    /**
     * Process webhook with idempotency and comprehensive error handling
     * Implements all Konnect best practices including rate limiting
     */
    public PaymentResponse processWebhook(String paymentRef, String requestId) {
        // Log all incoming webhook requests
        log.info("Processing webhook for payment_ref: {} with request_id: {}", paymentRef, requestId);

        try {


            // Fetch payment details
            PaymentResponse paymentResponse = konnectWebhookHandler.processWebhook(paymentRef);

            if (paymentResponse == null || paymentResponse.payment() == null) {
                log.error("Payment not found for reference: {}", paymentRef);
                throw new WebhookProcessingException("Payment not found", paymentRef);
            }

            // in a real-world scenario, you would validate the payment amount and currency with data from your database or business logic
            BigDecimal expectedAmount = new BigDecimal("100.00"); // Example expected amount
            KonnectToken expectedCurrency = KonnectToken.TND; // Example expected currency

            KonnectPaymentVerifier.verify(
                    paymentResponse,
                    expectedAmount,
                    expectedCurrency
            );

            // in a real-world scenario, you would also handle the payment status and update your database accordingly


            log.info("Webhook processed successfully for payment_ref: {} with status: {}",
                    paymentRef, paymentResponse.payment().status());

            return paymentResponse;


        } catch (InvalidPaymentReferenceException | WebhookProcessingException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error processing webhook for payment_ref: {}", paymentRef, e);
            throw new WebhookProcessingException("Internal processing error: " + e.getMessage(), paymentRef, e);
        }
    }


}
