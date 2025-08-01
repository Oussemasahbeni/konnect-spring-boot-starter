package com.oussemasahbeni.konnect.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum KonnectPaymentMethod {
    WALLET("wallet"),
    BANK_CARD("bank_card"),
    E_DINAR("e-DINAR"),
    KONNECT("konnect");
    private final String value;

    KonnectPaymentMethod(String value) {
        this.value = value;
    }


    @JsonValue
    public String getValue() {
        return value;
    }
}
