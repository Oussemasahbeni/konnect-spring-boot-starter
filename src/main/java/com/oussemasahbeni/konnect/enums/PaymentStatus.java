package com.oussemasahbeni.konnect.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentStatus {

    COMPLETED("completed"),
    PENDING("pending");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
