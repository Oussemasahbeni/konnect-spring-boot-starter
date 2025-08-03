# Konnect Spring Boot Starter

[![Build Status](https://img.shields.io/github/actions/workflow/status/Oussemasahbeni/konnect-spring-boot-starter/build.yml?branch=main)](https://github.com/Oussemasahbeni/konnect-spring-boot-starter/actions)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.oussemasahbeni/konnect-spring-boot-starter.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.oussemasahbeni%22%20AND%20a:%22konnect-spring-boot-starter%22)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

A simple and robust Spring Boot starter for integrating with
the [Konnect Payment Gateway](https://docs.konnect.network/docs/en/overview/getting-started). This library handles
low-level HTTP communication, authentication, and error handling, allowing you to focus on your application's business
logic.

It includes production-grade features like auto-configuration, webhook handling, payment verification utilities, and
optional resilience patterns (rate limiting and retries).

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
    - [Installation](#installation)
    - [Configuration](#configuration)
- [Core Usage - `KonnectTemplate`](#core-usage---konnecttemplate)
    - [Injecting the Template](#injecting-the-template)
    - [Initiating a Payment](#initiating-a-payment)
    - [Getting Payment Details](#getting-payment-details)
- [Handling Webhooks](#handling-webhooks)
- [Verifying Payments](#verifying-payments)
- [Advanced: Resilience (Rate Limiting & Retries)](#advanced-resilience-rate-limiting--retries)
- [Error Handling](#error-handling)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Auto-Configuration:** Automatically configures `RestClient`, `KonnectTemplate`, and other necessary beans.
- **Simplified API:** Provides a high-level `KonnectTemplate` to abstract away API complexities.
- **Secure Webhook Handling:** Includes a `KonnectWebhookHandler` to securely process incoming webhooks.
- **Payment Verification:** Offers a `KonnectPaymentVerifier` utility to enforce payment best practices.
- **Resilience Out-of-the-Box:** Optional, automatic rate limiting and retries powered by Resilience4j.
- **Customizable:** Allows for easy overriding of default payment parameters.
- **Robust Error Handling:** Provides specific, unchecked exceptions for API and business logic errors.

## Getting Started

### Installation

Add the starter dependency to your project.

**Maven:**

```xml

<dependency>
    <groupId>io.github.oussemasahbeni</groupId>
    <artifactId>konnect-spring-boot-starter</artifactId>
    <version>LATEST_VERSION</version> <!-- Replace with the latest version -->
</dependency>
```

**Gradle:**

```groovy
implementation 'io.github.oussemasahbeni:konnect-spring-boot-starter:LATEST_VERSION' // Replace with the latest version
```

### Configuration

Add your Konnect API credentials and default settings to your `application.yml` file.

```yaml
konnect:
  api:
    # --- Required Properties ---
    key: "your-konnect-api-key"                 # Your secret API Key from the Konnect dashboard.
    receiver-wallet-id: "your-konnect-wallet-id"  # The ID of the wallet receiving the payments.

    # --- Optional Properties ---
    # The base URL for the Konnect API. Defaults to the sandbox environment.
    # For production, use: https://api.konnect.network/api/v2/
    base-url: "https://api.sandbox.konnect.network/api/v2/"

    # URL for receiving webhook notifications.
    webhook-url: "https://your-app.com/api/konnect-webhooks/payment-status"

    # Default values for payment initiation. These can be overridden on a per-call basis.
    defaults:
      token: "TND"                         # Default currency (e.g., TND, USD).
      type: "immediate"                    # Default payment type.
      lifespan: 1800                       # Default payment lifespan in seconds (e.g., 30 minutes).
      theme: "light"                       # Default checkout theme ('light' or 'dark').
      add-payment-fees-to-amount: false    # Whether to add Konnect fees to the total amount.
      accepted-payment-methods: # List of default accepted payment methods.
        - "bank_card"
        - "wallet"
```

## Core Usage - `KonnectTemplate`

The `KonnectTemplate` is the primary entry point for all interactions with the Konnect API.

### Injecting the Template

Simply `@Autowired` or use constructor injection for the `KonnectTemplate` bean in your service.

```java
import io.github.oussemasahbeni.konnect.core.KonnectTemplate;
import org.springframework.stereotype.Service;

@Service
public class MyPaymentService {

    private final KonnectTemplate konnectTemplate;

    public MyPaymentService(KonnectTemplate konnectTemplate) {
        this.konnectTemplate = konnectTemplate;
    }

    // ... your business logic ...
}
```

### Initiating a Payment

#### Basic Payment

To initiate a payment using all the defaults from your `application.yml`:

```java
import java.math.BigDecimal;

import io.github.oussemasahbeni.konnect.model.InitKonnectPaymentResponse;

// ...

public InitKonnectPaymentResponse createSimplePayment(BigDecimal amount) {
    // This will use the default wallet ID, webhook URL, currency, etc.
    return konnectTemplate.initiatePayment(amount);
}
```

#### Payment with Customizations

You can easily override any default property using the builder customizer:

```java
import io.github.oussemasahbeni.konnect.model.enums.KonnectTheme;

// ...

public InitKonnectPaymentResponse createCustomPayment(BigDecimal amount) {
    return konnectTemplate.initiatePayment(amount, builder ->
            builder.theme(KonnectTheme.DARK)
                    .description("Special payment with a dark theme")
                    .orderId("my-custom-order-id-456")
    );
}
```

### Getting Payment Details

```java
import io.github.oussemasahbeni.konnect.model.PaymentResponse;

// ...

public PaymentResponse getPaymentStatus(String paymentRef) {
    return konnectTemplate.getPaymentDetails(paymentRef);
}
```

## Handling Webhooks

This library provides a `KonnectWebhookHandler` to make processing incoming webhooks simple and secure. It encapsulates
the best practice of using the `payment_ref` to make a secure API call and fetch the authoritative payment status.

**1. Create a `RestController` in your application:**
**2. Inject the `KonnectWebhookHandler` and call `processWebhook`.**

```java
package com.yourcompany.yourapp.webhooks;

import io.github.oussemasahbeni.konnect.core.KonnectWebhookHandler;
import io.github.oussemasahbeni.konnect.model.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/konnect-webhooks") // The path must match your configured webhook-url
public class MyKonnectWebhookController {

    private final KonnectWebhookHandler webhookHandler;

    public MyKonnectWebhookController(KonnectWebhookHandler webhookHandler) {
        this.webhookHandler = webhookHandler;
    }

    @GetMapping("/payment-status")
    public ResponseEntity<String> handlePaymentUpdate(@RequestParam("payment_ref") String paymentRef) {
        try {
            // Securely process the webhook to get the trusted payment status
            PaymentResponse paymentDetails = webhookHandler.processWebhook(paymentRef);

            // TODO: Implement your business logic here (e.g., verify and update order)
            // See the "Verifying Payments" section below for best practices.

            return ResponseEntity.ok("Webhook Processed");

        } catch (Exception e) {
            // Log the error and return a 500 status so Konnect may retry
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
```

## Verifying Payments

The Konnect documentation outlines several best practices for verifying a payment's integrity. To make this easy and
prevent common errors, the library provides a static `KonnectPaymentVerifier` utility.

It checks that:

1. The overall payment status is `completed`.
2. A `success` transaction exists.
3. The amount matches what you expected.
4. The currency matches what you expected.

### Usage Example

Here is how you would use the verifier within your webhook handler or a polling service.

```java
import io.github.oussemasahbeni.konnect.core.KonnectPaymentVerifier;
import io.github.oussemasahbeni.konnect.exception.PaymentVerificationException;
import io.github.oussemasahbeni.konnect.model.PaymentResponse;
import io.github.oussemasahbeni.konnect.model.enums.KonnectToken;

import java.math.BigDecimal;

// ... inside your service ...

public void confirmOrder(String paymentRef) {
    // 1. Get your order details from your database
    Order myOrder = orderRepository.findByPaymentRef(paymentRef);
    BigDecimal expectedAmount = myOrder.getAmount(); // e.g., new BigDecimal("20.000")
    KonnectToken expectedCurrency = KonnectToken.TND;

    try {
        // 2. Fetch the latest payment status
        PaymentResponse response = konnectTemplate.getPaymentDetails(paymentRef);

        // 3. Perform all best-practice checks with a single method call
        KonnectPaymentVerifier.verify(response, expectedAmount, expectedCurrency);

        // 4. If no exception is thrown, the payment is valid. Fulfill the order.
        myOrder.setStatus("PAID");
        orderRepository.save(myOrder);

    } catch (PaymentVerificationException e) {
        // This is a business-level failure (e.g., amount mismatch, not yet completed).
        log.warn("Payment verification failed for {}: {}", paymentRef, e.getMessage());
    } catch (KonnectApiException e) {
        // This is a lower-level API or network failure.
        log.error("API call failed for {}: {}", paymentRef, e.getMessage());
    }
}
```

## Handling Idempotency for Webhooks

Webhook endpoints can sometimes receive the same event more than once. To prevent duplicate processing (e.g., shipping
an order twice), your endpoint must be **idempotent**.

The recommended way to handle idempotency is to **use your own database as the source of truth**. Before processing a
webhook, check the status of the corresponding order in your database. If it has already been processed, you can safely
ignore the webhook.

The library guides this pattern by separating concerns. Your business service, which owns the database logic, should
implement this check.

### Example with Idempotency Check

Here is a full example of a user's service that correctly implements idempotency using the order's status.

```java

@Slf4j
@Service
public class WebhookService {

    private final KonnectWebhookHandler konnectWebhookHandler;
    private final OrderRepository orderRepository; // Your application's repository

    // ... constructor ...

    @Transactional
    public void processAndVerifyWebhook(String paymentRef) {
        // 1. Find the order in your database
        Order order = orderRepository.findByPaymentRef(paymentRef)
                .orElseThrow(() -> new WebhookProcessingException("Order not found"));

        // 2. IDEMPOTENCY CHECK: If already paid, ignore the duplicate webhook.
        if ("PAID".equals(order.getStatus())) {
            log.warn("Duplicate webhook for already paid order: {}", order.getId());
            return;
        }

        // 3. Securely fetch and verify the payment details
        PaymentResponse paymentResponse = konnectWebhookHandler.processWebhook(paymentRef);
        KonnectPaymentVerifier.verify(
                paymentResponse,
                order.getExpectedAmount(),
                order.getExpectedCurrency()
        );

        // 4. Mark as processed: Update the order status in your database
        log.info("Payment for order {} verified. Marking as PAID.", order.getId());
        order.setStatus("PAID");
        orderRepository.save(order);
    }
}
```

## Advanced: Resilience (Rate Limiting & Retries)

To protect your application from API rate limits and temporary network failures, this starter provides optional,
production-grade resilience patterns powered by Resilience4j.

### How to Enable

**1. Add Dependencies to your `pom.xml`:**

```xml

<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot3</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

**2. Configure in `application.yml`:**
The instance name (`konnect-api`) must match the one used internally by the library.

```yaml
resilience4j:
  # --- Rate Limiter Configuration ---
  ratelimiter:
    instances:
      konnect-api:
        limit-for-period: 95            # Stay safely below Konnect's 100 request/minute limit
        limit-refresh-period: 60s       # The period is 1 minute
        timeout-duration: 2s            # How long a thread will wait for a permit

  # --- Automatic Retry Configuration ---
  retry:
    instances:
      konnect-api:
        max-attempts: 3                 # Try the original call + 2 retries
        wait-duration: 500ms            # Initial wait time before the first retry
        enable-exponential-backoff: true
        exponential-backoff-multiplier: 2 # Wait times: 500ms -> 1s -> 2s
        # This predicate ensures we only retry on 5xx server errors, not 4xx client errors
        retry-exception-predicate: io.github.oussemasahbeni.konnect.autoconfigure.KonnectRetryExceptionPredicate
```

## Error Handling

The library uses a hierarchy of unchecked exceptions to signal errors.

- **`KonnectApiException`**: The base exception for any error returned by the Konnect API (HTTP 4xx or 5xx status
  codes). You can inspect the status code and error body.
- **`InvalidPaymentReferenceException`**: Thrown by `KonnectTemplate` if a payment reference has an invalid format
  *before* an API call is made.
- **`PaymentVerificationException`**: Thrown by `KonnectPaymentVerifier` when a payment fails a best-practice check (
  e.g., amount mismatch).

## Contributing

Contributions are welcome! Please feel free to submit a pull request or open an issue for any bugs, improvements, or
feature requests.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.