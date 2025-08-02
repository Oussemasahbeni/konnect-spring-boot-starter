package com.oussemasahbeni.konnect.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oussemasahbeni.konnect.model.enums.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KonnectPayment(
        @JsonProperty("transactions") List<Transaction> transactions,
        @JsonProperty("failedTransactions") int failedTransactions,
        @JsonProperty("successfulTransactions") int successfulTransactions,
        @JsonProperty("acceptedPaymentMethods") List<KonnectPaymentMethod> acceptedKonnectPaymentMethods,
        @JsonProperty("amount") BigDecimal amount,
        @JsonProperty("token") KonnectToken konnectToken,
        @JsonProperty("orderId") String orderId,
        @JsonProperty("type") KonnectPaymentType type,
        @JsonProperty("status") KonnectPaymentStatus status,
        @JsonProperty("convertedAmount") Long convertedAmount,
        @JsonProperty("exchangeRate") BigDecimal exchangeRate,
        @JsonProperty("paymentDetails") PaymentDetails paymentDetails,
        @JsonProperty("webhook") String webhook,
        @JsonProperty("createdAt") Instant createdAt,
        @JsonProperty("updatedAt") Instant updatedAt,
        @JsonProperty("id") String id
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Transaction(
            @JsonProperty("type") String type,
            @JsonProperty("method") KonnectPaymentMethod method,
            @JsonProperty("status") KonnectTransactionStatus status,
            @JsonProperty("token") KonnectToken token,
            @JsonProperty("amount") Long amount,
            @JsonProperty("ext_payment_ref") String extPaymentRef,
            @JsonProperty("from") String from,
            @JsonProperty("amountAfterFee") Long amountAfterFee,
            @JsonProperty("details") String details,
            @JsonProperty("extSenderInfo") ExtSenderInfo extSenderInfo,
            @JsonProperty("feeRate") BigDecimal feeRate,
            @JsonProperty("totalFee") Long totalFee,
            @JsonProperty("id") String id
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ExtSenderInfo(
            @JsonProperty("pan") String pan,
            @JsonProperty("expiration") String expiration,
            @JsonProperty("regionType") String regionType,
            @JsonProperty("email") String email
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PaymentDetails(
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("email") String email,
            @JsonProperty("name") String name
    ) {
    }
}





