package com.oussemasahbeni.konnect.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the successful response from the Initiate Payment API.
 * It contains the URL for the payment gateway and a unique reference for the transaction.
 *
 * @param payUrl     The payment gateway URL where the client can complete the payment.
 * @param paymentRef The unique reference ID for the payment.
 */
public record InitKonnectPaymentResponse(
        @JsonProperty("payUrl")
        String payUrl,
        @JsonProperty("paymentRef")
        String paymentRef) {
}