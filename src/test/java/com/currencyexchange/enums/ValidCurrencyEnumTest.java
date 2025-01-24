package com.currencyexchange.enums;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.currencyexchange.exception.UnsupportedCurrencyException;
import org.junit.jupiter.api.Test;

public class ValidCurrencyEnumTest {

  @Test
  void testIsValidCurrency_validCurrency() {
    assertDoesNotThrow(() -> ValidCurrencyEnum.isValidCurrency("USD"),
        "Valid currency 'USD' should not throw an exception");
    assertDoesNotThrow(() -> ValidCurrencyEnum.isValidCurrency("EUR"),
        "Valid currency 'EUR' should not throw an exception");
    assertDoesNotThrow(() -> ValidCurrencyEnum.isValidCurrency("gbp"),
        "Valid currency 'gbp' should not throw an exception");
  }

  @Test
  void testIsValidCurrency_shouldThrowExceptionForInvalidCurrencyy() {
    String invalidCurrency = "XYZ";

    UnsupportedCurrencyException exception = assertThrows(
        UnsupportedCurrencyException.class,
        () -> ValidCurrencyEnum.isValidCurrency(invalidCurrency)
    );

    assertEquals("Currency " + invalidCurrency + " is not supported", exception.getMessage(),
        "The exception should contain the correct message for an unsupported currency");
  }

  @Test
  void testIsValidCurrency_shouldThrowExceptionForNullInput() {
    UnsupportedCurrencyException exception = assertThrows(
        UnsupportedCurrencyException.class,
        () -> ValidCurrencyEnum.isValidCurrency(null)
    );

    assertEquals("Currency null is not supported", exception.getMessage(),
        "The exception should contain the correct message for null input");
  }

  @Test
  void testIsValidCurrency_shouldThrowExceptionForEmptyString() {
    UnsupportedCurrencyException exception = assertThrows(
        UnsupportedCurrencyException.class,
        () -> ValidCurrencyEnum.isValidCurrency("")
    );

    assertEquals("Currency  is not supported", exception.getMessage(),
        "The exception should contain the correct message for an empty string input");
  }
}
