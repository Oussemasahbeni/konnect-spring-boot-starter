package io.github.oussemasahbeni.konnect.exception;

/**
 * Exception thrown when a payment reference has an invalid format.
 * This exception is thrown before making API calls to prevent unnecessary
 * network requests with malformed payment references.
 * 
 * <p>Konnect payment references must be 24-character hexadecimal strings
 * (MongoDB ObjectIDs). This exception indicates that the provided reference
 * does not meet this format requirement.
 * 
 * @see io.github.oussemasahbeni.konnect.core.PaymentRefValidator#validate(String)
 */
public class InvalidPaymentReferenceException extends RuntimeException {

    private final String paymentRef;

    /**
     * Constructs a new InvalidPaymentReferenceException with a message and the invalid reference.
     * 
     * @param message the error message describing why the reference is invalid
     * @param paymentRef the invalid payment reference that caused this exception
     */
    public InvalidPaymentReferenceException(String message, String paymentRef) {
        super(message);
        this.paymentRef = paymentRef;
    }

    /**
     * Constructs a new InvalidPaymentReferenceException with a message, payment reference, and underlying cause.
     * 
     * @param message the error message describing why the reference is invalid
     * @param paymentRef the invalid payment reference that caused this exception
     * @param cause the underlying cause of this exception
     */
    public InvalidPaymentReferenceException(String message, String paymentRef, Throwable cause) {
        super(message, cause);
        this.paymentRef = paymentRef;
    }

    /**
     * Gets the invalid payment reference that caused this exception.
     * 
     * @return the invalid payment reference
     */
    public String getPaymentRef() {
        return paymentRef;
    }
}
