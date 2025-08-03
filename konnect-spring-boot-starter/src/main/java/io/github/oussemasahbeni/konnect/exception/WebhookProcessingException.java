package io.github.oussemasahbeni.konnect.exception;

/**
 * Exception thrown when webhook processing fails.
 * This exception is typically thrown by {@link io.github.oussemasahbeni.konnect.core.KonnectWebhookHandler}
 * when there are issues processing incoming webhook notifications from Konnect.
 * 
 * <p>Common webhook processing failures include:
 * <ul>
 *   <li>Missing or invalid payment reference in webhook parameters</li>
 *   <li>Failure to fetch payment details from the API</li>
 *   <li>Network connectivity issues during webhook processing</li>
 *   <li>Malformed webhook data</li>
 * </ul>
 * 
 * @see io.github.oussemasahbeni.konnect.core.KonnectWebhookHandler
 */
public class WebhookProcessingException extends RuntimeException {

    private final String paymentRef;
    private final String errorCode;

    /**
     * Constructs a new WebhookProcessingException with a message and payment reference.
     * The error code defaults to "WEBHOOK_PROCESSING_ERROR".
     * 
     * @param message the error message describing what went wrong
     * @param paymentRef the payment reference being processed when the error occurred
     */
    public WebhookProcessingException(String message, String paymentRef) {
        super(message);
        this.paymentRef = paymentRef;
        this.errorCode = "WEBHOOK_PROCESSING_ERROR";
    }

    /**
     * Constructs a new WebhookProcessingException with a message, payment reference, and custom error code.
     * 
     * @param message the error message describing what went wrong
     * @param paymentRef the payment reference being processed when the error occurred
     * @param errorCode a custom error code for categorizing the type of webhook processing failure
     */
    public WebhookProcessingException(String message, String paymentRef, String errorCode) {
        super(message);
        this.paymentRef = paymentRef;
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new WebhookProcessingException with a message, payment reference, and underlying cause.
     * The error code defaults to "WEBHOOK_PROCESSING_ERROR".
     * 
     * @param message the error message describing what went wrong
     * @param paymentRef the payment reference being processed when the error occurred
     * @param cause the underlying cause of this exception
     */
    public WebhookProcessingException(String message, String paymentRef, Throwable cause) {
        super(message, cause);
        this.paymentRef = paymentRef;
        this.errorCode = "WEBHOOK_PROCESSING_ERROR";
    }

    /**
     * Gets the payment reference that was being processed when this exception occurred.
     * 
     * @return the payment reference
     */
    public String getPaymentRef() {
        return paymentRef;
    }

    /**
     * Gets the error code categorizing the type of webhook processing failure.
     * 
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}
