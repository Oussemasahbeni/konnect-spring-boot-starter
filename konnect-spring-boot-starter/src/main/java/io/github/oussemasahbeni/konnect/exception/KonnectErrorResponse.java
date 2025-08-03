package io.github.oussemasahbeni.konnect.exception;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents the structured error response returned by the Konnect API.
 * When the API returns an error status (4xx or 5xx), the response body
 * contains this structured error information.
 * 
 * <p>This record provides access to detailed error information that can be
 * used for debugging, logging, and user-friendly error messages.
 * 
 * @param errors A list of specific error details, each containing a message and optional code
 * 
 * @see KonnectErrorDetail
 * @see io.github.oussemasahbeni.konnect.exception.KonnectApiException
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KonnectErrorResponse(
        List<KonnectErrorDetail> errors
) {
}