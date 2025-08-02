package com.oussemasahbeni.konnect.utils;

public class PaymentRefValidator {

    private PaymentRefValidator() {
    }

    /**
     * Validate payment reference format based on Konnect API specification
     * Konnect payment references are MongoDB ObjectIDs (24 character hex strings)
     *
     * @param paymentRef the payment reference to validate
     * @return true if the payment reference is valid, false otherwise
     */
    public static boolean isValid(String paymentRef) {
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
