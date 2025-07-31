package com.oussemasahbeni.konnect.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Represents the top-level error response payload from the Konnect API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KonnectErrorResponse(
        List<KonnectErrorDetail> errors
) {
}