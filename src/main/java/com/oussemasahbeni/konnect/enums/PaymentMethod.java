package com.oussemasahbeni.konnect.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentMethod {
    WALLET("wallet"),
    BANK_CARD("bank_card"),
    E_DINAR("e-DINAR"),
    KONNECT("konnect");
    private final String value;
    PaymentMethod(String value) {
        this.value = value;
    }


    @JsonValue
    public String getValue() {
        return value;
    }
}
