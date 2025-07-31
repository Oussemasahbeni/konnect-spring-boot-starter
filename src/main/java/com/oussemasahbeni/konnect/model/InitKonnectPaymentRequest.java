package com.oussemasahbeni.konnect.model;


import com.oussemasahbeni.konnect.enums.PaymentMethod;
import com.oussemasahbeni.konnect.enums.PaymentType;
import com.oussemasahbeni.konnect.enums.Theme;
import com.oussemasahbeni.konnect.enums.Token;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents the request body for the Initiate Payment API endpoint.
 * This class is used to create a payment request with all the necessary details
 * for Konnect to process a transaction.  It includes a fluid builder for easy object creation.
 */
public class InitKonnectPaymentRequest {
    /**
     * Konnect wallet ID of the payment receiver.
     */
    private String receiverWalletId;

    /**
     * The currency of the payment.
     */
    private Token token;

    /**
     * The payment amount in the smallest currency unit (e.g., Millimes for TND).
     */
    private BigDecimal amount;

    /**
     * The type of payment, e.g., immediate or partial.
     */
    private PaymentType type;

    /**
     * A description of the payment that will be visible to the payer.
     */
    private String description;

    /**
     * A list of payment methods accepted for this transaction.
     */
    private List<PaymentMethod> acceptedPaymentMethods;

    /**
     * The expiration time for the payment link, in minutes.
     */
    private int lifespan;

    /**
     * If true, the payer must fill out a checkout form before payment.
     */
    private boolean checkoutForm;

    /**
     * If true, Konnect's payment fees are added to the total amount paid by the client. Defaults to false.
     */
    private boolean addPaymentFeesToAmount;

    /**
     * The first name of the payer.
     */
    private String firstName;

    /**
     * The last name of the payer.
     */
    private String lastName;

    /**
     * The phone number of the payer.
     */
    private String phoneNumber;

    /**
     * The email address of the payer.
     */
    private String email;

    /**
     * A custom identifier for the order from the merchant's system.
     */
    private String orderId;

    /**
     * The URL that Konnect will send a GET request to for payment notifications.
     */
    private String webhook;

    /**
     * The visual theme for the payment gateway page, e.g., light or dark.
     */
    private Theme theme;

    // Getters and Setters
    public String getReceiverWalletId() {
        return receiverWalletId;
    }

    public void setReceiverWalletId(String receiverWalletId) {
        this.receiverWalletId = receiverWalletId;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PaymentMethod> getAcceptedPaymentMethods() {
        return acceptedPaymentMethods;
    }

    public void setAcceptedPaymentMethods(List<PaymentMethod> acceptedPaymentMethods) {
        this.acceptedPaymentMethods = acceptedPaymentMethods;
    }

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    public boolean isCheckoutForm() {
        return checkoutForm;
    }

    public void setCheckoutForm(boolean checkoutForm) {
        this.checkoutForm = checkoutForm;
    }

    public boolean isAddPaymentFeesToAmount() {
        return addPaymentFeesToAmount;
    }

    public void setAddPaymentFeesToAmount(boolean addPaymentFeesToAmount) {
        this.addPaymentFeesToAmount = addPaymentFeesToAmount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    /**
     * Builder class for {@link InitKonnectPaymentRequest}.
     */
    public static class Builder {
        private String receiverWalletId;
        private Token token;
        private BigDecimal amount;
        private PaymentType type;
        private String description;
        private List<PaymentMethod> acceptedPaymentMethods;
        private int lifespan;
        private boolean checkoutForm;
        private boolean addPaymentFeesToAmount;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String email;
        private String orderId;
        private String webhook;
        private Theme theme;

        public Builder receiverWalletId(String receiverWalletId) {
            this.receiverWalletId = receiverWalletId;
            return this;
        }

        public Builder token(Token token) {
            this.token = token;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder type(PaymentType type) {
            this.type = type;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder acceptedPaymentMethods(List<PaymentMethod> acceptedPaymentMethods) {
            this.acceptedPaymentMethods = acceptedPaymentMethods;
            return this;
        }

        public Builder lifespan(int lifespan) {
            this.lifespan = lifespan;
            return this;
        }

        public Builder checkoutForm(boolean checkoutForm) {
            this.checkoutForm = checkoutForm;
            return this;
        }

        public Builder addPaymentFeesToAmount(boolean addPaymentFeesToAmount) {
            this.addPaymentFeesToAmount = addPaymentFeesToAmount;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder webhook(String webhook) {
            this.webhook = webhook;
            return this;
        }

        public Builder theme(Theme theme) {
            this.theme = theme;
            return this;
        }

        public InitKonnectPaymentRequest build() {
            InitKonnectPaymentRequest request = new InitKonnectPaymentRequest();
            request.setReceiverWalletId(this.receiverWalletId);
            request.setToken(this.token);
            request.setAmount(this.amount);
            request.setType(this.type);
            request.setDescription(this.description);
            request.setAcceptedPaymentMethods(this.acceptedPaymentMethods);
            request.setLifespan(this.lifespan);
            request.setCheckoutForm(this.checkoutForm);
            request.setAddPaymentFeesToAmount(this.addPaymentFeesToAmount);
            request.setFirstName(this.firstName);
            request.setLastName(this.lastName);
            request.setPhoneNumber(this.phoneNumber);
            request.setEmail(this.email);
            request.setOrderId(this.orderId);
            request.setWebhook(this.webhook);
            request.setTheme(this.theme);
            return request;
        }
    }
}