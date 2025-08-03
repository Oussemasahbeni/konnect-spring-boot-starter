package io.github.oussemasahbeni.konnect.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum KonnectTransactionStatus {

    SUCCESS("success"),
    FAILED_PAYMENT("failed_payment");

    private final String value;

    KonnectTransactionStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
