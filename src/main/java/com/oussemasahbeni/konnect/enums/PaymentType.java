package com.oussemasahbeni.konnect.enums;


import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentType {
    IMMEDIATE("immediate"),
    PARTIAL("partial");

    private final String value;

    PaymentType(String value) {
        this.value = value;
    }


    @JsonValue
    public String getValue() {
        return value;
    }
}
