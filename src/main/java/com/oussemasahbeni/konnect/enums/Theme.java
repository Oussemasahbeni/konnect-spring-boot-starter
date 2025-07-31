package com.oussemasahbeni.konnect.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Theme {

    LIGHT("light"),
    DARK("dark");

    private final String value;

    Theme(String value) {
        this.value = value;
    }


    @JsonValue
    public String getValue() {
        return value;
    }
}
