package io.github.oussemasahbeni.konnect.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a single, specific error detail from the Konnect API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KonnectErrorDetail(
        String code,
        String target,
        String message
) {
}