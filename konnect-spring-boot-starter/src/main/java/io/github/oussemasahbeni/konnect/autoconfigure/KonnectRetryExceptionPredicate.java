package io.github.oussemasahbeni.konnect.autoconfigure;

import java.util.function.Predicate;

import org.springframework.http.HttpStatus;

import io.github.oussemasahbeni.konnect.exception.KonnectApiException;

/**
 * Custom exception predicate for Resilience4j retry mechanism.
 * This predicate determines which exceptions should trigger a retry attempt
 * when making calls to the Konnect API.
 * 
 * <p>The retry strategy follows best practices:
 * <ul>
 *   <li><strong>Retry on 5xx server errors</strong> - These are typically transient issues on the server side</li>
 *   <li><strong>Do NOT retry on 4xx client errors</strong> - These indicate problems with the request that won't be resolved by retrying</li>
 *   <li><strong>Retry on other network issues</strong> - Connectivity problems, timeouts, etc.</li>
 * </ul>
 * 
 * <p>This predicate is used in the Resilience4j configuration:
 * <pre>
 * resilience4j:
 *   retry:
 *     instances:
 *       konnect-api:
 *         retry-exception-predicate: io.github.oussemasahbeni.konnect.autoconfigure.KonnectRetryExceptionPredicate
 * </pre>
 * 
 * @see io.github.oussemasahbeni.konnect.exception.KonnectApiException
 */
public class KonnectRetryExceptionPredicate implements Predicate<Throwable> {
    
    /**
     * Tests whether the given throwable should trigger a retry attempt.
     * 
     * @param throwable the exception that occurred during the API call
     * @return true if the operation should be retried, false otherwise
     */
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