package com.oussemasahbeni.konnect.exception;

/**
 * Exception thrown when payment reference is invalid
 */
public class InvalidPaymentReferenceException extends RuntimeException {

    private final String paymentRef;

    public InvalidPaymentReferenceException(String message, String paymentRef) {
        super(message);
        this.paymentRef = paymentRef;
    }

    public InvalidPaymentReferenceException(String message, String paymentRef, Throwable cause) {
        super(message, cause);
        this.paymentRef = paymentRef;
    }

    public String getPaymentRef() {
        return paymentRef;
    }
}
