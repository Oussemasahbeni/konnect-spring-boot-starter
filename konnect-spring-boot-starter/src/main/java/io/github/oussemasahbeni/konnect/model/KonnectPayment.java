package io.github.oussemasahbeni.konnect.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.oussemasahbeni.konnect.model.enums.KonnectPaymentMethod;
import io.github.oussemasahbeni.konnect.model.enums.KonnectPaymentStatus;
import io.github.oussemasahbeni.konnect.model.enums.KonnectPaymentType;
import io.github.oussemasahbeni.konnect.model.enums.KonnectToken;
import io.github.oussemasahbeni.konnect.model.enums.KonnectTransactionStatus;

/**
 * Represents a complete payment entity from the Konnect API.
 * This record contains all the information about a payment including its transactions,
 * status, amounts, and associated metadata.
 * 
 * @param transactions The list of transactions associated with this payment
 * @param failedTransactions The number of failed transactions
 * @param successfulTransactions The number of successful transactions  
 * @param acceptedKonnectPaymentMethods The list of accepted payment methods for this payment
 * @param amount The payment amount in the specified currency
 * @param konnectToken The currency token (e.g., TND, USD, EUR)
 * @param orderId The unique order identifier
 * @param type The payment type (immediate or partial)
 * @param status The current payment status (completed, pending, expired)
 * @param convertedAmount The amount converted to the smallest currency unit
 * @param exchangeRate The exchange rate used for currency conversion
 * @param paymentDetails Additional payment details including customer information
 * @param webhook The webhook URL for payment notifications
 * @param createdAt The timestamp when the payment was created
 * @param updatedAt The timestamp when the payment was last updated
 * @param id The unique payment identifier
 */
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
    
    /**
     * Represents a single transaction within a payment.
     * Each payment can contain multiple transactions, and this record contains
     * the details of each individual transaction attempt.
     * 
     * @param type The transaction type
     * @param method The payment method used for this transaction
     * @param status The transaction status (success or failed_payment)
     * @param token The currency token for this transaction
     * @param amount The transaction amount in the smallest currency unit
     * @param extPaymentRef The external payment reference
     * @param from The source of the payment
     * @param amountAfterFee The amount after deducting fees
     * @param details Additional transaction details
     * @param extSenderInfo Extended sender information
     * @param feeRate The fee rate applied to this transaction
     * @param totalFee The total fee amount
     * @param id The unique transaction identifier
     */
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

    /**
     * Extended sender information for transactions.
     * Contains additional details about the sender's payment method and location.
     * 
     * @param pan The masked payment card number (Primary Account Number)
     * @param expiration The payment card expiration date
     * @param regionType The geographical region type of the sender
     * @param email The sender's email address
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ExtSenderInfo(
            @JsonProperty("pan") String pan,
            @JsonProperty("expiration") String expiration,
            @JsonProperty("regionType") String regionType,
            @JsonProperty("email") String email
    ) {
    }

    /**
     * Payment details containing customer information.
     * This record holds the customer's contact information associated with the payment.
     * 
     * @param phoneNumber The customer's phone number
     * @param email The customer's email address
     * @param name The customer's full name
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PaymentDetails(
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("email") String email,
            @JsonProperty("name") String name
    ) {
    }
}





