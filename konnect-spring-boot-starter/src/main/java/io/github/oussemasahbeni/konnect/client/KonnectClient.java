package io.github.oussemasahbeni.konnect.client;


import java.io.IOException;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.oussemasahbeni.konnect.exception.KonnectApiException;
import io.github.oussemasahbeni.konnect.exception.KonnectErrorResponse;
import io.github.oussemasahbeni.konnect.model.InitKonnectPaymentRequest;
import io.github.oussemasahbeni.konnect.model.InitKonnectPaymentResponse;
import io.github.oussemasahbeni.konnect.model.PaymentResponse;

/**
 * Low-level HTTP client for communicating with the Konnect Payment Gateway API.
 * This class handles the direct HTTP communication, authentication, and error handling
 * for all Konnect API endpoints. It provides the foundational layer for higher-level
 * services like {@link io.github.oussemasahbeni.konnect.core.KonnectTemplate}.
 * 
 * <p>This client automatically handles:
 * <ul>
 *   <li>Authentication via API key headers</li>
 *   <li>JSON serialization/deserialization</li>
 *   <li>HTTP error status code handling</li>
 *   <li>Konnect-specific error response parsing</li>
 * </ul>
 * 
 * @see io.github.oussemasahbeni.konnect.core.KonnectTemplate
 */
public class KonnectClient {

    private final RestClient restClient;

    /**
     * Constructs a new KonnectClient with the provided RestClient and ObjectMapper.
     * The RestClient should be pre-configured with the Konnect API base URL and authentication headers.
     * 
     * @param konnectRestClient A pre-configured RestClient with base URL and authentication
     * @param objectMapper The ObjectMapper for JSON processing and error handling
     */
    public KonnectClient(RestClient konnectRestClient, ObjectMapper objectMapper) {
        this.restClient = konnectRestClient.mutate()
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> handleApiError(objectMapper, response))
                .build();
    }


    /**
     * Calls the Konnect API to initiate a new payment.
     *
     * @param paymentRequest The payment request object.
     * @return The response from the Konnect API, containing the payment URL and reference.
     */
    public InitKonnectPaymentResponse initiatePayment(InitKonnectPaymentRequest paymentRequest) {
        return restClient.post()
                .uri("/payments/init-payment")
                .body(paymentRequest)
                .retrieve()
                .body(InitKonnectPaymentResponse.class);

    }


    /**
     * Get payment details for a given payment reference.
     * This method fetches the payment details from the Konnect API using the provided payment reference.
     *
     * @param paymentRef The reference of the payment to retrieve details for.
     * @return PaymentResponse containing the details of the payment.
     * @throws KonnectApiException if the API call fails or returns an error status.
     */
    public PaymentResponse getPaymentDetails(String paymentRef) {
        return restClient.get()
                .uri("/payments/" + paymentRef)
                .retrieve()
                .body(PaymentResponse.class);


    }


    /**
     * Handles API errors by reading the response body and throwing a KonnectApiException.
     *
     * @param objectMapper The ObjectMapper to parse the error response.
     * @param response     The ClientHttpResponse containing the error details.
     * @throws IOException if there is an error reading the response body.
     */
    private static void handleApiError(ObjectMapper objectMapper, ClientHttpResponse response) throws IOException {
        try {
            byte[] bodyBytes = StreamUtils.copyToByteArray(response.getBody());
            if (bodyBytes.length == 0) {
                String msg = "Konnect API Error: No response body (status: " + response.getStatusCode() + ")";
                throw new KonnectApiException(msg, response.getStatusCode().value());
            }

            KonnectErrorResponse errorBody = objectMapper.readValue(bodyBytes, KonnectErrorResponse.class);
            String message = "Konnect API Error: " + errorBody.errors().getFirst().message();
            throw new KonnectApiException(message, response.getStatusCode().value(), errorBody);
        } catch (IOException e) {
            String msg = "Konnect API Error: Unable to parse error response (status: " + response.getStatusCode() + ")";
            throw new KonnectApiException(msg, response.getStatusCode().value(), null, e);
        }
    }


}
