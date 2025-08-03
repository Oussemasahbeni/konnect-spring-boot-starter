package io.github.oussemasahbeni.konnect.core;

/**
 * Utility class for validating Konnect payment reference formats.
 * This class provides static methods to validate that payment references
 * conform to the expected format before making API calls.
 * 
 * <p>Konnect payment references are MongoDB ObjectIDs, which are 24-character
 * hexadecimal strings. This validator ensures that references match this format
 * to prevent unnecessary API calls with invalid references.
 * 
 * @see io.github.oussemasahbeni.konnect.core.KonnectTemplate#getPaymentDetails(String)
 */
public class PaymentRefValidator {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private PaymentRefValidator() {
    }

    /**
     * Validate payment reference format based on Konnect API specification
     * Konnect payment references are MongoDB ObjectIDs (24 character hex strings)
     *
     * @param paymentRef the payment reference to validate
     * @return true if the payment reference is valid, false otherwise
     */
    public static boolean validate(String paymentRef) {
        if (paymentRef == null || paymentRef.trim().isBlank()) {
            return false;
        }

        paymentRef = paymentRef.trim();

        if (paymentRef.length() != 24) {
            return false;
        }
        return paymentRef.matches("^[a-fA-F0-9]{24}$");
    }
}
