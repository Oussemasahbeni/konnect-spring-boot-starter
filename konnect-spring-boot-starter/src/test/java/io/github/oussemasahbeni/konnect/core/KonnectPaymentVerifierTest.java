package io.github.oussemasahbeni.konnect.core;

import io.github.oussemasahbeni.konnect.exception.PaymentVerificationException;
import io.github.oussemasahbeni.konnect.model.KonnectPayment;
import io.github.oussemasahbeni.konnect.model.PaymentResponse;
import io.github.oussemasahbeni.konnect.model.enums.KonnectPaymentMethod;
import io.github.oussemasahbeni.konnect.model.enums.KonnectPaymentStatus;
import io.github.oussemasahbeni.konnect.model.enums.KonnectToken;
import io.github.oussemasahbeni.konnect.model.enums.KonnectTransactionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static io.github.oussemasahbeni.konnect.core.KonnectPaymentVerifier.verify;
import static io.github.oussemasahbeni.konnect.model.enums.KonnectToken.TND;
import static org.junit.jupiter.api.Assertions.*;

class KonnectPaymentVerifierTest {


    @Test
    @DisplayName("Should return the successful transaction when all verification rules pass")
    void verify_whenAllConditionsAreMet_shouldReturnSuccessfulTransaction() {
        // Arrange
        BigDecimal expectedAmount = new BigDecimal(20_000);
        KonnectToken expectedCurrency = TND;
        PaymentResponse validResponse = createTestPaymentResponse(
                KonnectPaymentStatus.COMPLETED,
                KonnectTransactionStatus.SUCCESS,
                expectedAmount,
                expectedCurrency
        );

        // Act & Assert
        KonnectPayment.Transaction result = assertDoesNotThrow(
                () -> verify(validResponse, expectedAmount, expectedCurrency),
                "Verification should not throw an exception for a valid payment."
        );

        assertNotNull(result);
        assertEquals(KonnectTransactionStatus.SUCCESS, result.status());
    }


    @Test
    @DisplayName("Should throw exception when overall payment status is not COMPLETED")
    void verify_whenPaymentStatusIsNotCompleted_shouldThrowException() {
        // Arrange

        BigDecimal amount = new BigDecimal(20_000);

        PaymentResponse pendingResponse = createTestPaymentResponse(
                KonnectPaymentStatus.PENDING,
                KonnectTransactionStatus.SUCCESS,
                amount,
                TND
        );

        // Act & Assert
        PaymentVerificationException exception = assertThrows(
                PaymentVerificationException.class,
                () -> verify(pendingResponse, amount, TND)
        );

        assertTrue(exception.getMessage().contains("not 'completed'"), "Exception message should explain the status mismatch.");
    }

    @Test
    @DisplayName("Should throw exception when no successful transaction exists")
    void verify_whenNoSuccessfulTransactionExists_shouldThrowException() {
        // Arrange
        BigDecimal amount = new BigDecimal(20_000);

        PaymentResponse failedTxResponse = createTestPaymentResponse(
                KonnectPaymentStatus.COMPLETED,
                KonnectTransactionStatus.FAILED_PAYMENT,
                amount,
                TND
        );

        // Act & Assert
        PaymentVerificationException exception = assertThrows(
                PaymentVerificationException.class,
                () -> verify(failedTxResponse, amount, TND)
        );

        assertTrue(exception.getMessage().contains("contains no completed transaction"), "Exception message should explain the missing transaction.");
    }

    @Test
    @DisplayName("Should throw exception when payment amount does not match expected amount")
    void verify_whenAmountMismatches_shouldThrowException() {
        // Arrange
        BigDecimal actualAmount = new BigDecimal("20.000");
        BigDecimal expectedAmount = new BigDecimal("19.999"); // <-- The mismatch
        PaymentResponse response = createTestPaymentResponse(
                KonnectPaymentStatus.COMPLETED,
                KonnectTransactionStatus.SUCCESS,
                actualAmount, // The amount in the payment object
                TND
        );

        // Act & Assert
        PaymentVerificationException exception = assertThrows(
                PaymentVerificationException.class,
                () -> verify(response, expectedAmount, TND) // Use the different expected amount
        );

        assertTrue(exception.getMessage().contains("Amount mismatch"), "Exception message should explain the amount difference.");
    }

    @Test
    @DisplayName("Should throw exception when transaction currency does not match expected currency")
    void verify_whenCurrencyMismatches_shouldThrowException() {
        // Arrange
        KonnectToken expectedCurrency = KonnectToken.USD;
        BigDecimal amount = new BigDecimal(20_000);
        PaymentResponse response = createTestPaymentResponse(
                KonnectPaymentStatus.COMPLETED,
                KonnectTransactionStatus.SUCCESS,
                amount,
                TND // The currency in the payment object
        );

        // Act & Assert
        PaymentVerificationException exception = assertThrows(
                PaymentVerificationException.class,
                () -> verify(response, amount, expectedCurrency) // Use the different expected currency
        );

        assertTrue(exception.getMessage().contains("Currency mismatch"), "Exception message should explain the currency difference.");
    }

    @Test
    @DisplayName("Should throw NullPointerException for null response")
    void verify_whenResponseIsNull_shouldThrowNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> verify(null, BigDecimal.ONE, TND)
        );
    }


    /**
     * A factory method to create a test PaymentResponse object.
     * This reduces code duplication across tests.
     */
    private PaymentResponse createTestPaymentResponse(KonnectPaymentStatus paymentStatus,
                                                      KonnectTransactionStatus transactionStatus,
                                                      BigDecimal amount,
                                                      KonnectToken currency) {
        KonnectPayment.Transaction transaction = new KonnectPayment.Transaction(
                "ePayment", KonnectPaymentMethod.BANK_CARD, transactionStatus, currency, amount.movePointRight(3).longValue(),
                "ext-ref", "from", 0L, "details", null, BigDecimal.ZERO, 0L, "tx-id-123"
        );

        KonnectPayment payment = new KonnectPayment(
                List.of(transaction), 0, 1, Collections.emptyList(), amount,
                null, "order-123", null, paymentStatus, 0L, BigDecimal.ONE,
                null, "webhook-url", null, null, "payment-id-123"
        );

        return new PaymentResponse(payment);
    }
}