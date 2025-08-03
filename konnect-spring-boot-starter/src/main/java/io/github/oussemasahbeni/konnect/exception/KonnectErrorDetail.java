package io.github.oussemasahbeni.konnect.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents an individual error detail within a Konnect API error response.
 * Each error detail provides specific information about what went wrong,
 * including error codes, target fields, and human-readable messages.
 * 
 * @param code The error code identifying the specific type of error (optional)
 * @param target The field or parameter that caused the error (optional)
 * @param message A human-readable description of the error
 * 
 * @see KonnectErrorResponse
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KonnectErrorDetail(
        String code,
        String target,
        String message
) {
}