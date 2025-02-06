package com.currencyexchange.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FixerResponseTest {

  @Test
  void getDescription_shouldReturnCorrectDescription() {
    Map<String, BigDecimal> rates =
        Map.of(
            "USD", new BigDecimal("1.0"),
            "EUR", new BigDecimal("0.85"));
    FixerResponse fixerResponse =
        FixerResponse.builder().base("USD").date("2025-02-04").rates(rates).build();

    assertEquals("USD", fixerResponse.getBase());
    assertEquals("2025-02-04", fixerResponse.getDate());
  }

  @Test
  void getDescription_shouldHandleNullRates() {
    FixerResponse fixerResponse =
        FixerResponse.builder().base("USD").date("2025-02-04").rates(null).build();

    assertEquals("USD", fixerResponse.getBase());
    assertEquals("2025-02-04", fixerResponse.getDate());
    assertNull(fixerResponse.getRates());
  }
}
