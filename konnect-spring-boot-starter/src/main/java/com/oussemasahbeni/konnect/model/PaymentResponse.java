package com.oussemasahbeni.konnect.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentResponse(@JsonProperty("payment") KonnectPayment payment) {
}
