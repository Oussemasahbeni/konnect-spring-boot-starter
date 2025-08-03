package io.github.oussemasahbeni.konnect.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum KonnectTheme {

    LIGHT("light"),
    DARK("dark");

    private final String value;

    KonnectTheme(String value) {
        this.value = value;
    }


    @JsonValue
    public String getValue() {
        return value;
    }
}
