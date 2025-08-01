package com.oussemasahbeni.konnect.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum KonnectPaymentStatus {

    COMPLETED("completed"),
    PENDING("pending");

    private final String value;

    KonnectPaymentStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
