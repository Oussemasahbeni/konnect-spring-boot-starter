package com.oussemasahbeni.konnect.integration;


import com.oussemasahbeni.konnect.autoconfigure.KonnectProperties;
import com.oussemasahbeni.konnect.client.KonnectClient;
import com.oussemasahbeni.konnect.core.KonnectTemplate;
import com.oussemasahbeni.konnect.exception.InvalidPaymentReferenceException;
import com.oussemasahbeni.konnect.model.InitKonnectPaymentRequest;
import com.oussemasahbeni.konnect.model.PaymentResponse;
import com.oussemasahbeni.konnect.model.enums.KonnectPaymentType;
import com.oussemasahbeni.konnect.model.enums.KonnectTheme;
import com.oussemasahbeni.konnect.model.enums.KonnectToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KonnectTemplateTest {


    @Mock
    private KonnectClient mockKonnectClient;

    private KonnectTemplate konnectTemplate;

    @BeforeEach
    void setUp() {
        KonnectProperties testProperties = new KonnectProperties(
                "https://api.sandbox.konnect.network/api/v2/",
                "dummy-key",
                "default-wallet-id-from-props",
                "https://default.com/webhook",
                new KonnectProperties.KonnectPaymentDefaults(
                        KonnectToken.TND,
                        KonnectPaymentType.IMMEDIATE,
                        List.of(),
                        30,
                        true,
                        KonnectTheme.LIGHT,
                        false
                )
        );

        konnectTemplate = new KonnectTemplate(mockKonnectClient, testProperties);
    }

    @Test
    void initiatePayment_shouldApplyDefaultProperties() {
        // Arrange
        BigDecimal amount = new BigDecimal("50.00");

        // This ArgumentCaptor will "capture" the object that is passed to our mock client
        ArgumentCaptor<InitKonnectPaymentRequest> requestCaptor = ArgumentCaptor.forClass(InitKonnectPaymentRequest.class);

        // Act
        konnectTemplate.initiatePayment(amount);

        // Assert
        // Verify that the initiatePayment method on our mock was called exactly once,
        // and capture the argument it was called with.
        verify(mockKonnectClient).initiatePayment(requestCaptor.capture());

        InitKonnectPaymentRequest capturedRequest = requestCaptor.getValue();

        // Now, we can assert that the template correctly built the request!
        assertEquals(amount, capturedRequest.getAmount());
        assertEquals("default-wallet-id-from-props", capturedRequest.getReceiverWalletId()); // PROOF!
        assertEquals("https://default.com/webhook", capturedRequest.getWebhook());       // PROOF!
    }

    @Test
    void initiatePayment_whenUserCustomizes_shouldOverrideDefaults() {
        // Arrange
        BigDecimal amount = new BigDecimal("100.00");
        String customWalletId = "OVERRIDDEN-WALLET-ID";
        String customWebhook = "https://custom.com/webhook";

        ArgumentCaptor<InitKonnectPaymentRequest> requestCaptor = ArgumentCaptor.forClass(InitKonnectPaymentRequest.class);

        // Act
        konnectTemplate.initiatePayment(amount, builder -> {
            builder.receiverWalletId(customWalletId);
            builder.webhook(customWebhook);
        });

        // Assert
        verify(mockKonnectClient).initiatePayment(requestCaptor.capture());

        InitKonnectPaymentRequest capturedRequest = requestCaptor.getValue();

        // Verify that the user's custom values were used instead of the defaults
        assertEquals(amount, capturedRequest.getAmount());
        assertEquals(customWalletId, capturedRequest.getReceiverWalletId());
        assertEquals(customWebhook, capturedRequest.getWebhook());
    }

    @Test
    void getPaymentDetails_withInvalidReference_throwsExceptionBeforeCallingClient() {
        // Arrange
        String invalidRef = "this-is-not-a-valid-ref";

        // Act & Assert
        // Verify that our specific validation exception is thrown.
        InvalidPaymentReferenceException exception = assertThrows(
                InvalidPaymentReferenceException.class,
                () -> konnectTemplate.getPaymentDetails(invalidRef)
        );

        // Optionally, assert on the exception details
        assertEquals("Invalid payment reference format", exception.getMessage());
        assertEquals(invalidRef, exception.getPaymentRef());

        // CRITICAL: Verify that the KonnectClient was NEVER called because our
        // validation guard clause worked correctly. This proves we are "failing fast".
        verify(mockKonnectClient, never()).getPaymentDetails(anyString());
    }

    @Test
    void getPaymentDetails_withValidReference_delegatesToClientAndReturnsResponse() {
        // Arrange
        String validRef = "68891e9415c9b9a0dae24829"; // A valid format

        // Create a fake response that we expect our MOCK client to return.
        PaymentResponse expectedResponse = new PaymentResponse(null);

        // Tell Mockito: "WHEN mockKonnectClient.getPaymentDetails is called with the validRef,
        // THEN return our fake expectedResponse."
        when(mockKonnectClient.getPaymentDetails(validRef)).thenReturn(expectedResponse);

        // Act
        // Call the method on the class we are testing.
        PaymentResponse actualResponse = konnectTemplate.getPaymentDetails(validRef);

        // Assert
        // 1. Verify that the response from the template is the one we told the mock to return.
        //    This proves the template is correctly passing through the result.
        assertEquals(expectedResponse, actualResponse, "The response from the template should match the one from the client.");

        // 2. Verify that the method on our mock client was called exactly once with the correct argument.
        //    This proves the template is correctly delegating the call.
        verify(mockKonnectClient, times(1)).getPaymentDetails(validRef);
    }
}
