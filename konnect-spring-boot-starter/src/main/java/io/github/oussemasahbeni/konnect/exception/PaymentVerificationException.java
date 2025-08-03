package io.github.oussemasahbeni.konnect.exception;

/**
 * Exception thrown when payment verification fails according to Konnect best practices.
 * This exception is thrown by {@link io.github.oussemasahbeni.konnect.core.KonnectPaymentVerifier}
 * when a payment does not meet the expected criteria for a valid, completed payment.
 * 
 * <p>Common verification failures include:
 * <ul>
 *   <li>Payment status is not "completed"</li>
 *   <li>No successful transactions found</li>
 *   <li>Payment amount doesn't match expected amount</li>
 *   <li>Payment currency doesn't match expected currency</li>
 * </ul>
 * 
 * <p>This is typically a business logic error indicating that the payment
 * should not be considered valid for order fulfillment.
 * 
 * @see io.github.oussemasahbeni.konnect.core.KonnectPaymentVerifier
 */
public class PaymentVerificationException extends RuntimeException {
    
    /**
     * Constructs a new PaymentVerificationException with the specified message.
     * 
     * @param message the detail message explaining why verification failed
     */
    public PaymentVerificationException(String message) {
        super(message);
    }
}
