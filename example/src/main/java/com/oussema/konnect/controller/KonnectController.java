package com.oussema.konnect.controller;


import com.oussema.konnect.dto.InitPaymentDto;
import com.oussema.konnect.service.PaymentService;
import com.oussemasahbeni.konnect.model.InitKonnectPaymentResponse;
import com.oussemasahbeni.konnect.model.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/konnect")
@CrossOrigin(origins = "http://localhost:4200")
public class KonnectController {

    private final PaymentService paymentService;

    public KonnectController(PaymentService paymentService) {
        this.paymentService = paymentService;
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


}
