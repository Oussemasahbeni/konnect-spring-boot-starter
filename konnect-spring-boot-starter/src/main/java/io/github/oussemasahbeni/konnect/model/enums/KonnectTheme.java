package io.github.oussemasahbeni.konnect.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the available UI themes for the Konnect payment checkout form.
 * This enum allows customization of the visual appearance of the payment interface.
 */
public enum KonnectTheme {

    /** Light color scheme for the checkout form */
    LIGHT("light"),
    
    /** Dark color scheme for the checkout form */
    DARK("dark");

    private final String value;

    /**
     * Constructs a KonnectTheme with the specified value.
     * 
     * @param value the string representation of the theme
     */
    KonnectTheme(String value) {
        this.value = value;
    }

    /**
     * Gets the string value of this theme.
     * 
     * @return the string representation used in JSON serialization
     */
    @JsonValue
    public String getValue() {
        return value;
    }
}
