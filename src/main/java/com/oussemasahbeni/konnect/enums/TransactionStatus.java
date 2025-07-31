package com.oussemasahbeni.konnect.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionStatus {

    SUCCESS("success"),
    FAILED_PAYMENT("failed_payment");

    private final String value;

    TransactionStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
