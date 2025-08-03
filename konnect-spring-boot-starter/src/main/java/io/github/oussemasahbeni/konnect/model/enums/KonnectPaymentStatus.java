package io.github.oussemasahbeni.konnect.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the possible statuses of a Konnect payment.
 * This enum defines the lifecycle states that a payment can be in.
 */
public enum KonnectPaymentStatus {

    /** The payment has been completed successfully */
    COMPLETED("completed"),
    
    /** The payment is still pending and awaiting processing */
    PENDING("pending"),
    
    /** The payment has expired and is no longer valid */
    EXPIRED("expired");

    private final String value;

    /**
     * Constructs a KonnectPaymentStatus with the specified value.
     * 
     * @param value the string representation of the payment status
     */
    KonnectPaymentStatus(String value) {
        this.value = value;
    }

    /**
     * Gets the string value of this payment status.
     * 
     * @return the string representation used in JSON serialization
     */
    @JsonValue
    public String getValue() {
        return value;
    }
}
