package com.oussemasahbeni.konnect.model.enums;


import com.fasterxml.jackson.annotation.JsonValue;

public enum KonnectPaymentType {
    IMMEDIATE("immediate"),
    PARTIAL("partial");

    private final String value;

    KonnectPaymentType(String value) {
        this.value = value;
    }


    @JsonValue
    public String getValue() {
        return value;
    }
}
