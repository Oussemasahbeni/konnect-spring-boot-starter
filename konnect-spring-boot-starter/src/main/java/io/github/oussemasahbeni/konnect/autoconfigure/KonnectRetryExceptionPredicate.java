package io.github.oussemasahbeni.konnect.autoconfigure;

import io.github.oussemasahbeni.konnect.exception.KonnectApiException;
import org.springframework.http.HttpStatus;

import java.util.function.Predicate;

public class KonnectRetryExceptionPredicate implements Predicate<Throwable> {
    @Override
    public boolean test(Throwable throwable) {
        if (throwable instanceof KonnectApiException e) {
            // Do NOT retry on client errors (4xx), as they won't succeed.
            // Only retry on server errors (5xx).
            return HttpStatus.valueOf(e.getStatusCode()).is5xxServerError();
        }
        // Retry on other potential transient network issues.
        return true;
    }
}