package com.oussema.konnect.exception;

import com.oussemasahbeni.konnect.exception.InvalidPaymentReferenceException;
import com.oussemasahbeni.konnect.exception.KonnectApiException;
import com.oussemasahbeni.konnect.exception.WebhookProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // A more detailed error response for your own API's clients
    public record ApiErrorResponse(
            String message,
            int status,
            String timestamp,
            String path,
            String errorCode
    ) {
    }

    @ExceptionHandler(KonnectApiException.class)
    public ResponseEntity<ApiErrorResponse> handleKonnectApiException(KonnectApiException ex, WebRequest request) {
        log.error("Konnect API error: {}", ex.getMessage(), ex);

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                ex.getMessage(),
                HttpStatus.FAILED_DEPENDENCY.value(),
                LocalDateTime.now().toString(),
                request.getDescription(false),
                "KONNECT_API_ERROR"
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.FAILED_DEPENDENCY);
    }

    @ExceptionHandler(WebhookProcessingException.class)
    public ResponseEntity<ApiErrorResponse> handleWebhookProcessingException(WebhookProcessingException ex, WebRequest request) {
        log.error("Webhook processing error for payment_ref {}: {}", ex.getPaymentRef(), ex.getMessage(), ex);

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now().toString(),
                request.getDescription(false),
                ex.getErrorCode()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidPaymentReferenceException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidPaymentReferenceException(InvalidPaymentReferenceException ex, WebRequest request) {
        log.error("Invalid payment reference {}: {}", ex.getPaymentRef(), ex.getMessage(), ex);

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now().toString(),
                request.getDescription(false),
                "INVALID_PAYMENT_REFERENCE"
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                "An unexpected error occurred. Please contact support.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now().toString(),
                request.getDescription(false),
                "INTERNAL_ERROR"
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}