package io.github.oussemasahbeni.konnect.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the response received when querying payment details from the Konnect API.
 * This record wraps the complete payment information returned by the API.
 * 
 * @param payment The complete payment details including transactions, status, and metadata
 */
public record PaymentResponse(@JsonProperty("payment") KonnectPayment payment) {
}
