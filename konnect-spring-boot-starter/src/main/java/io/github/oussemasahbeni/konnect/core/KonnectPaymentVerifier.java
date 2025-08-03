package io.github.oussemasahbeni.konnect.core;

import io.github.oussemasahbeni.konnect.exception.PaymentVerificationException;
import io.github.oussemasahbeni.konnect.model.KonnectPayment;
import io.github.oussemasahbeni.konnect.model.PaymentResponse;
import io.github.oussemasahbeni.konnect.model.enums.KonnectToken;
import io.github.oussemasahbeni.konnect.model.enums.KonnectTransactionStatus;

import java.math.BigDecimal;
import java.util.Objects;

import static io.github.oussemasahbeni.konnect.model.enums.KonnectPaymentStatus.COMPLETED;

/**
 * A dedicated service that implements Konnect's best practices for verifying a payment's integrity.
 */
public class KonnectPaymentVerifier {


    private KonnectPaymentVerifier() {
        // Private constructor to prevent instantiation
    }

    /**
     * Verifies a payment against expected values, checking status, amount, and currency.
     * Throws a specific exception if any validation fails.
     * RULE #1: Always verify the payment status is 'completed'.
     * RULE #2: Ensure the transaction status is 'success'.
     * RULE #3: Validate the amount matches what you expected.
     * RULE #4: Confirm the currency matches what you expected.
     *
     * @param paymentResponse  The full payment details object fetched from the Konnect API.
     * @param expectedAmount   The amount your system expected the user to pay.
     * @param expectedCurrency The currency code (e.g., "TND") your system expected.
     * @return The completed and verified Transaction object for your records.
     * @throws PaymentVerificationException if any best practice check fails.
     */
    public static KonnectPayment.Transaction verify(PaymentResponse paymentResponse, BigDecimal expectedAmount, KonnectToken expectedCurrency) {
        Objects.requireNonNull(paymentResponse, "PaymentResponse cannot be null.");
        Objects.requireNonNull(expectedAmount, "Expected amount cannot be null.");

        // Verify the overall payment status is 'completed'.
        if (!COMPLETED.equals(paymentResponse.payment().status())) {
            throw new PaymentVerificationException("Payment status is '" + paymentResponse.payment().status() + "', not 'completed'.");
        }

        //  Find the specific transaction also 'success'.
        KonnectPayment.Transaction completedTransaction = paymentResponse.payment().transactions().stream()
                .filter(tx -> KonnectTransactionStatus.SUCCESS.equals(tx.status()))
                .findFirst()
                .orElseThrow(() -> new PaymentVerificationException("Payment is marked 'completed' but contains no completed transaction."));

        //  Verify that the amount received matches exactly what you expected.
        if (paymentResponse.payment().amount().compareTo(expectedAmount) != 0) {
            throw new PaymentVerificationException(String.format(
                    "Amount mismatch: Expected %.3f but API reports reached amount of %.3f.",
                    expectedAmount, paymentResponse.payment().amount()
            ));
        }

        // Verify the currency is what you expected.
        if (!expectedCurrency.equals(completedTransaction.token())) {
            throw new PaymentVerificationException(String.format(
                    "Currency mismatch: Expected '%s' but transaction was in '%s'.",
                    expectedCurrency, completedTransaction.token()
            ));
        }

        return completedTransaction;
    }
}