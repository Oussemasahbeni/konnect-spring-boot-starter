package io.github.oussemasahbeni.konnect.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the possible statuses of individual transactions within a Konnect payment.
 * This enum defines whether a specific transaction attempt was successful or failed.
 */
public enum KonnectTransactionStatus {

    /** The transaction completed successfully */
    SUCCESS("success"),
    
    /** The transaction failed during payment processing */
    FAILED_PAYMENT("failed_payment");

    private final String value;

    /**
     * Constructs a KonnectTransactionStatus with the specified value.
     * 
     * @param value the string representation of the transaction status
     */
    KonnectTransactionStatus(String value) {
        this.value = value;
    }

    /**
     * Gets the string value of this transaction status.
     * 
     * @return the string representation used in JSON serialization
     */
    @JsonValue
    public String getValue() {
        return value;
    }
}
