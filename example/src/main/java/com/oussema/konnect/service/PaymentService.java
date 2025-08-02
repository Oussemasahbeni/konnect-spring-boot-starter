package com.oussema.konnect.service;

import com.oussema.konnect.dto.InitPaymentDto;
import com.oussemasahbeni.konnect.core.KonnectTemplate;
import com.oussemasahbeni.konnect.model.InitKonnectPaymentResponse;
import com.oussemasahbeni.konnect.model.PaymentResponse;
import com.oussemasahbeni.konnect.model.enums.KonnectTheme;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final KonnectTemplate konnectTemplate;

    public PaymentService(KonnectTemplate konnectTemplate) {
        this.konnectTemplate = konnectTemplate;
    }


    public InitKonnectPaymentResponse initPayment(InitPaymentDto initPaymentDto) {
        return konnectTemplate.initiatePayment(initPaymentDto.amount());
    }

    public InitKonnectPaymentResponse initPaymentWithLightTheme(InitPaymentDto initPaymentDto) {
        return konnectTemplate.initiatePayment(initPaymentDto.amount(), builder ->
                builder.theme(KonnectTheme.DARK)
                        .description("Special dark theme payment")
        );
    }

    public PaymentResponse getPaymentDetails(String paymentRef) {
        return konnectTemplate.getPaymentDetails(paymentRef);
    }
}
