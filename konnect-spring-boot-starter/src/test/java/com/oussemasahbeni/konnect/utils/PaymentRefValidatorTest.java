package com.oussemasahbeni.konnect.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentRefValidatorTest {


    @Test
    void testValidPaymentRef() {
        String validRef = "68891e9415c9b9a0dae24829";
        assertTrue(PaymentRefValidator.isValid(validRef), "Expected valid payment reference to pass validation");
    }

    @Test
    void testInvalidPaymentRef() {
        String invalidRef = "68891e9415c9b9a0dae2482"; // 23 characters, should be invalid
        assertFalse(PaymentRefValidator.isValid(invalidRef), "Expected invalid payment reference to fail validation");
    }

    @Test
    void testNullPaymentRef() {
        String nullRef = null;
        assertFalse(PaymentRefValidator.isValid(nullRef), "Expected null payment reference to fail validation");
    }

    @Test
    void testEmptyPaymentRef() {
        String emptyRef = "";
        assertFalse(PaymentRefValidator.isValid(emptyRef), "Expected empty payment reference to fail validation");
    }

    @Test
    void testBlankPaymentRef() {
        String blankRef = "   ";
        assertFalse(PaymentRefValidator.isValid(blankRef), "Expected blank payment reference to fail validation");

    }

    @Test
    void testPaymentRefWithInvalidCharacters() {
        String invalidRef = "68891e9415c9b9a0dae2482G"; // Contains 'G', should be invalid
        assertFalse(PaymentRefValidator.isValid(invalidRef), "Expected payment reference with invalid characters to fail validation");
    }

    @Test
    void testPaymentRefWithMixedCase() {
        String mixedCaseRef = "68891e9415C9b9A0DAE24829"; // Valid hex string with mixed case
        assertTrue(PaymentRefValidator.isValid(mixedCaseRef), "Expected mixed case payment reference to pass validation");
    }

    @Test
    void testPaymentRefWithLeadingAndTrailingSpaces() {
        String spacedRef = " 68891e9415c9b9a0dae24829 "; // Valid hex string with leading and trailing spaces
        assertTrue(PaymentRefValidator.isValid(spacedRef), "Expected payment reference with leading and trailing spaces to pass validation");
    }

}