package com.oussemasahbeni.konnect.exception;


public class KonnectApiException extends RuntimeException {

    private final int statusCode;
    private final KonnectErrorResponse errorResponse;


    public KonnectApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorResponse = null;
    }

    public KonnectApiException(String message, int statusCode, KonnectErrorResponse errorResponse) {
        super(message);
        this.statusCode = statusCode;
        this.errorResponse = errorResponse;
    }

    public KonnectApiException(String message, int statusCode, KonnectErrorResponse errorResponse, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.errorResponse = errorResponse;
    }


    public KonnectErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public int getStatusCode() {
        return statusCode;
    }
}