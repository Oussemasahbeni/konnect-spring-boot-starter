package com.oussemasahbeni.konnect.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oussemasahbeni.konnect.model.enums.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KonnectPayment(
        List<Transaction> transactions,
        int failedTransactions,
        int successfulTransactions,
        List<KonnectPaymentMethod> acceptedKonnectPaymentMethods,
        Long amount,
        KonnectToken konnectToken,
        String orderId,
        KonnectPaymentType type,
        KonnectPaymentStatus status,
        Long convertedAmount,
        BigDecimal exchangeRate,
        PaymentDetails paymentDetails,
        String webhook,
        Instant createdAt,
        Instant updatedAt,
        String id
) {
    public record Transaction(
            String type,
            String method,
            KonnectTransactionStatus status,
            String token,
            Long amount,
            @JsonProperty("ext_payment_ref") String extPaymentRef,
            String from,
            Long amountAfterFee,
            String details,
            ExtSenderInfo extSenderInfo,
            BigDecimal feeRate,
            Long totalFee,
            String id
    ) {
    }

    public record ExtSenderInfo(
            String pan,
            String expiration,
            String regionType,
            String email
    ) {
    }

    public record PaymentDetails(
            String phoneNumber,
            String email,
            String name
    ) {
    }
}
