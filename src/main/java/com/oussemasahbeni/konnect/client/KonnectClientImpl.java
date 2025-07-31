package com.oussemasahbeni.konnect.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.oussemasahbeni.konnect.exception.KonnectApiException;
import com.oussemasahbeni.konnect.exception.KonnectErrorResponse;
import com.oussemasahbeni.konnect.model.InitKonnectPaymentRequest;
import com.oussemasahbeni.konnect.model.InitKonnectPaymentResponse;
import com.oussemasahbeni.konnect.model.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

import java.io.IOException;

public class KonnectClientImpl implements KonnectClient {


    private static final Logger log = LoggerFactory.getLogger(KonnectClientImpl.class);
    private final RestClient restClient;


    public KonnectClientImpl(RestClient konnectRestClient, ObjectMapper objectMapper) {
        this.restClient = konnectRestClient.mutate()
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
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
                        log.error(msg, e);
                        throw new KonnectApiException(msg, response.getStatusCode().value(), null, e);
                    }
                })
                .build();
    }

    /**
     * Calls the Konnect API to initiate a new payment.
     *
     * @param paymentRequest The payment request object.
     * @return The response from the Konnect API, containing the payment URL and reference.
     */
    @Override
    public InitKonnectPaymentResponse initiatePayment(InitKonnectPaymentRequest paymentRequest) {
        log.info("Initiating payment with Konnect API: {}", paymentRequest);
        ResponseEntity<InitKonnectPaymentResponse> responseEntity = restClient.post()
                .uri("/payments/init-payment")
                .body(paymentRequest)
                .retrieve()
                .toEntity(InitKonnectPaymentResponse.class);

        return responseEntity.getBody();
    }


    /**
     * Get payment details with caching (5 minute TTL)
     * Cached to reduce API calls and improve performance
     */
    @Cacheable(value = "paymentDetails", key = "#paymentRef")
    @Override
    public PaymentResponse getPaymentDetails(String paymentRef) {
        log.info("Fetching payment details for reference: {}", paymentRef);
        ResponseEntity<PaymentResponse> responseEntity = restClient.get()
                .uri("/payments/" + paymentRef)
                .retrieve()
                .toEntity(PaymentResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            throw new KonnectApiException("Failed to fetch payment details for reference: " + paymentRef, responseEntity.getStatusCode().value());
        }
    }
}
