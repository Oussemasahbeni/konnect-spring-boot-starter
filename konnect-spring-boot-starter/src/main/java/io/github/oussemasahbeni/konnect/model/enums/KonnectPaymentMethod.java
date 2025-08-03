package io.github.oussemasahbeni.konnect.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the different payment methods supported by the Konnect payment system.
 * These are the various ways customers can complete their payments.
 */
public enum KonnectPaymentMethod {
    
    /** Digital wallet payment method */
    WALLET("wallet"),
    
    /** Credit or debit card payment method */
    BANK_CARD("bank_card"),
    
    /** Tunisian e-DINAR digital currency */
    E_DINAR("e-DINAR"),
    
    /** Konnect's own payment method */
    KONNECT("konnect");
    
    private final String value;

    /**
     * Constructs a KonnectPaymentMethod with the specified value.
     * 
     * @param value the string representation of the payment method
     */
    KonnectPaymentMethod(String value) {
        this.value = value;
    }

    /**
     * Gets the string value of this payment method.
     * 
     * @return the string representation used in JSON serialization
     */
    @JsonValue
    public String getValue() {
        return value;
    }
}
