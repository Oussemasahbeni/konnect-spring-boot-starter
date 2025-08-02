package com.oussema.konnect.controller;


import com.oussema.konnect.dto.InitPaymentDto;
import com.oussema.konnect.service.PaymentService;
import com.oussema.konnect.service.WebhookService;
import com.oussemasahbeni.konnect.model.InitKonnectPaymentResponse;
import com.oussemasahbeni.konnect.model.PaymentResponse;
import com.oussemasahbeni.konnect.model.enums.KonnectPaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/konnect")
@CrossOrigin(origins = "http://localhost:4200")
public class KonnectController {

    private final PaymentService paymentService;
    private final WebhookService webhookService;

    public KonnectController(PaymentService paymentService, WebhookService webhookService) {
        this.paymentService = paymentService;
        this.webhookService = webhookService;
    }

    /**
     * Initialize payment with externalized defaults and optional user overrides
     *
     * @param initPaymentDto Payment DTO with amount (required) and optional overrides
     * @return Payment initialization response with payUrl and paymentRef
     */
    @PostMapping("/init-payment")
    public InitKonnectPaymentResponse createNewPayment(@RequestBody InitPaymentDto initPaymentDto) {
        log.info("Initiating payment for amount: {} with overrides: {}",
                initPaymentDto.amount(), initPaymentDto);
        return this.paymentService.initPayment(initPaymentDto);
    }

    @PostMapping("/init-payment-light-theme")
    public InitKonnectPaymentResponse createNewPaymentWithLightTheme(@RequestBody InitPaymentDto
                                                                             initPaymentDto) {
        log.info("Initiating payment with light theme for amount: {} with overrides: {}",
                initPaymentDto.amount(), initPaymentDto);
        return this.paymentService.initPaymentWithLightTheme(initPaymentDto);
    }

    @GetMapping("/payment-details/{paymentRef}")
    public PaymentResponse getPaymentDetails(@PathVariable String paymentRef) {
        log.info("Fetching payment details for reference: {}", paymentRef);
        return this.paymentService.getPaymentDetails(paymentRef);
    }


    @GetMapping("/webhook")
    public RedirectView handleWebhookWithRedirect(@RequestParam(name = "payment_ref") String paymentRef) {
        String requestId = UUID.randomUUID().toString();

        log.info("Webhook redirect received for payment_ref: {}, request_id: {}", paymentRef, requestId);

        try {
            PaymentResponse response = webhookService.processWebhook(paymentRef, requestId);

            String redirectUrl = response.payment().status().equals(KonnectPaymentStatus.COMPLETED)
                    ? "http://localhost:4200/payment-success"
                    : "http://localhost:4200/payment-error";

            log.info("Redirecting to: {} for payment_ref: {}", redirectUrl, paymentRef);
            return new RedirectView(redirectUrl);

        } catch (Exception e) {
            log.error("Error in webhook redirect for payment_ref: {}", paymentRef, e);
            return new RedirectView("http://localhost:4200/payment-error");
        }
    }


}
