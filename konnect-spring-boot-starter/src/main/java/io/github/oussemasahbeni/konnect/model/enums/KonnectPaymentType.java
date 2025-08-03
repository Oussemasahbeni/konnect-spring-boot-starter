package io.github.oussemasahbeni.konnect.model.enums;


import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the types of payments supported by the Konnect payment system.
 * This enum defines how the payment should be processed.
 */
public enum KonnectPaymentType {
    
    /** Payment that must be completed in full immediately */
    IMMEDIATE("immediate"),
    
    /** Payment that can be completed in multiple installments */
    PARTIAL("partial");

    private final String value;

    /**
     * Constructs a KonnectPaymentType with the specified value.
     * 
     * @param value the string representation of the payment type
     */
    KonnectPaymentType(String value) {
        this.value = value;
    }

    /**
     * Gets the string value of this payment type.
     * 
     * @return the string representation used in JSON serialization
     */
    @JsonValue
    public String getValue() {
        return value;
    }
}
