package io.github.oussemasahbeni.konnect.integration;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.oussemasahbeni.konnect.client.KonnectClient;
import io.github.oussemasahbeni.konnect.exception.KonnectApiException;
import io.github.oussemasahbeni.konnect.model.InitKonnectPaymentRequest;
import io.github.oussemasahbeni.konnect.model.InitKonnectPaymentResponse;
import io.github.oussemasahbeni.konnect.model.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClient;
import org.wiremock.spring.EnableWireMock;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(classes = TestApplication.class)
@TestPropertySource(properties = {
        "konnect.api.key=test-api-key",
        "konnect.api.receiver-wallet-id=test-wallet-id",
})
@EnableWireMock
class KonnectClientIntegrationTest {

    private KonnectClient konnectClient;

    @Value("${wiremock.server.baseUrl}")
    private String wiremockBaseUrl;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        RestClient restClient = RestClient.builder()
                .baseUrl(wiremockBaseUrl)
                .build();
        this.konnectClient = new KonnectClient(restClient, objectMapper);
    }


    @Test
    void getPaymentDetails_whenApiReturns200_shouldReturnPaymentResponse() {
        // Arrange
        String paymentRef = "valid-ref-123";
        String successJson = """
                { "id": "valid-ref-123", "status": "paid", "amount": 100 }
                """;
        stubFor(get(urlEqualTo("/payments/" + paymentRef))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(successJson)));

        // Act
        PaymentResponse response = konnectClient.getPaymentDetails(paymentRef);

        // Assert
        assertNotNull(response);
    }

    @Test
    void getPaymentDetails_whenApiReturns404_shouldThrowKonnectApiException() {
        // Arrange
        String paymentRef = "not-found-ref";
        String errorJson = """
                {
                  "errors": [
                     {
                       "code": "NOT_FOUND",
                       "target": "common",
                       "message": "Paiement non trouvé",
                       "source": {},
                       "extra": 1
                     }
                  ]
                }
                """;
        stubFor(get(urlEqualTo("/payments/" + paymentRef))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(errorJson)));

        // Act
        KonnectApiException ex = assertThrows(KonnectApiException.class, () -> {
            konnectClient.getPaymentDetails(paymentRef);
        });

        // Assert
        assertEquals(404, ex.getStatusCode());
        assertTrue(ex.getMessage().contains("Paiement non trouvé"));
    }

    @Test
    void initiatePayment_withValidRequest_returnsSuccessResponse() throws JsonProcessingException {
        // Arrange
        InitKonnectPaymentRequest request = new InitKonnectPaymentRequest();
        request.setAmount(BigDecimal.valueOf(1234));

        String successResponseJson = """
                {
                  "payUrl": "https://testpayurl.com",
                  "paymentRef": "test-payment-ref-1234"
                }
                """;

        stubFor(post(urlEqualTo("/payments/init-payment"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(request)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(successResponseJson)));

        // Act
        InitKonnectPaymentResponse response = konnectClient.initiatePayment(request);

        // Assert
        assertNotNull(response);
        assertEquals("test-payment-ref-1234", response.paymentRef());
        assertEquals("https://testpayurl.com", response.payUrl());
    }

    @Test
    void initiatePayment_withInvalidData_throwsKonnectApiException() {
        // Arrange: Create a request that the "API" will deem invalid
        InitKonnectPaymentRequest badRequest = new InitKonnectPaymentRequest.Builder()
                .amount(BigDecimal.valueOf(-100)) // Negative amount to trigger validation error
                .build();

        String errorJson = """
                {
                      "errors": [
                          {
                              "code": "is_invalid",
                              "target": "field",
                              "message": "Amount must be an integer number between 100 and 10000000.",
                              "source": {
                                  "field": "amount"
                              }
                          }
                      ]
                  }
                """;

        stubFor(post(urlEqualTo("/payments/init-payment"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(errorJson)));

        // Act & Assert
        KonnectApiException exception = assertThrows(KonnectApiException.class, () -> konnectClient.initiatePayment(badRequest));

        assertEquals(400, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Amount must be an integer number between 100 and 10000000."));
    }

}