package io.github.oussema.konnect.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * Payment initialization DTO with optional overrides for default configuration
 * All fields except amount are optional and will use defaults from configuration if not provided
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record InitPaymentDto(
        // Required fields
        BigDecimal amount
) {
}
