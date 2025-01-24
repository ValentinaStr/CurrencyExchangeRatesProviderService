package com.currencyexchange.enums;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ValidCurrencyEnumTest {
  @Test
  void testValidCurrency() {
    assertTrue(ValidCurrencyEnum.isValidCurrency("USD"));
    assertTrue(ValidCurrencyEnum.isValidCurrency("EUR"));
    assertTrue(ValidCurrencyEnum.isValidCurrency("GBP"));
    assertTrue(ValidCurrencyEnum.isValidCurrency("RUB"));
  }

  @Test
  void testInvalidCurrency() {
    assertFalse(ValidCurrencyEnum.isValidCurrency("INV"));
    assertFalse(ValidCurrencyEnum.isValidCurrency("123"));
  }

  @Test
  void testEmptyCurrency() {
    assertFalse(ValidCurrencyEnum.isValidCurrency(""));
  }

  @Test
  void testCurrencyCaseInsensitivity() {
    assertTrue(ValidCurrencyEnum.isValidCurrency("usd"));
    assertTrue(ValidCurrencyEnum.isValidCurrency("EUR"));
  }
}
