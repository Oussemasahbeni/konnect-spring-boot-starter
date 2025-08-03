package io.github.oussemasahbeni.konnect.exception;

/**
 * Exception thrown when the Konnect API returns an error response.
 * This is the base exception for all API-related errors including HTTP 4xx and 5xx responses.
 * 
 * <p>This exception provides access to:
 * <ul>
 *   <li>The HTTP status code returned by the API</li>
 *   <li>The structured error response from Konnect (if available)</li>
 *   <li>A human-readable error message</li>
 * </ul>
 * 
 * <p>Common scenarios that trigger this exception:
 * <ul>
 *   <li>Authentication failures (401)</li>
 *   <li>Invalid request parameters (400)</li>
 *   <li>Payment not found (404)</li>
 *   <li>Rate limiting (429)</li>
 *   <li>Server errors (5xx)</li>
 * </ul>
 */
public class KonnectApiException extends RuntimeException {

    private final int statusCode;
    private final KonnectErrorResponse errorResponse;

    /**
     * Constructs a new KonnectApiException with a message and status code.
     * 
     * @param message the error message
     * @param statusCode the HTTP status code returned by the API
     */
    public KonnectApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorResponse = null;
    }

    /**
     * Constructs a new KonnectApiException with a message, status code, and structured error response.
     * 
     * @param message the error message
     * @param statusCode the HTTP status code returned by the API
     * @param errorResponse the structured error response from the Konnect API
     */
    public KonnectApiException(String message, int statusCode, KonnectErrorResponse errorResponse) {
        super(message);
        this.statusCode = statusCode;
        this.errorResponse = errorResponse;
    }

    /**
     * Constructs a new KonnectApiException with a message, status code, error response, and underlying cause.
     * 
     * @param message the error message
     * @param statusCode the HTTP status code returned by the API
     * @param errorResponse the structured error response from the Konnect API
     * @param cause the underlying cause of this exception
     */
    public KonnectApiException(String message, int statusCode, KonnectErrorResponse errorResponse, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.errorResponse = errorResponse;
    }

    /**
     * Gets the structured error response from the Konnect API, if available.
     * 
     * @return the error response, or null if not available
     */
    public KonnectErrorResponse getErrorResponse() {
        return errorResponse;
    }

    /**
     * Gets the HTTP status code returned by the Konnect API.
     * 
     * @return the HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }
}