package com.oussemasahbeni.konnect.client;

import com.oussemasahbeni.konnect.model.InitKonnectPaymentRequest;
import com.oussemasahbeni.konnect.model.InitKonnectPaymentResponse;
import com.oussemasahbeni.konnect.model.PaymentResponse;

public interface KonnectClient {
    InitKonnectPaymentResponse initiatePayment(InitKonnectPaymentRequest paymentRequest);

    PaymentResponse getPaymentDetails(String paymentRef);
}
