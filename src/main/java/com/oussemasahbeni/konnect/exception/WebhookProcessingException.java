package com.oussemasahbeni.konnect.exception;

/**
 * Exception thrown when webhook processing fails
 */
public class WebhookProcessingException extends RuntimeException {

    private final String paymentRef;
    private final String errorCode;

    public WebhookProcessingException(String message, String paymentRef) {
        super(message);
        this.paymentRef = paymentRef;
        this.errorCode = "WEBHOOK_PROCESSING_ERROR";
    }

    public WebhookProcessingException(String message, String paymentRef, String errorCode) {
        super(message);
        this.paymentRef = paymentRef;
        this.errorCode = errorCode;
    }

    public WebhookProcessingException(String message, String paymentRef, Throwable cause) {
        super(message, cause);
        this.paymentRef = paymentRef;
        this.errorCode = "WEBHOOK_PROCESSING_ERROR";
    }

    public String getPaymentRef() {
        return paymentRef;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
