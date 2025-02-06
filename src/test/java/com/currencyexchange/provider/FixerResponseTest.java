package com.currencyexchange.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.Test;

class FixerResponseTest {

  @Test
  void getDescription_shouldReturnCorrectDescription() {
    Map<String, Double> rates =
        Map.of(
            "USD", 1.0,
            "EUR", 0.85);
    FixerResponse fixerResponse =
        FixerResponse.builder().base("USD").date("2025-02-04").rates(rates).build();

    String description = fixerResponse.getDescription();

    String expectedDescription = "FixerResponse{base='USD', rates={USD=1.0, EUR=0.85}}";

    assertTrue(description.contains("base='USD'"));
    assertTrue(description.contains("USD=1.0"));
    assertTrue(description.contains("EUR=0.85"));
  }

  @Test
  void getDescription_shouldHandleNullRates() {
    FixerResponse fixerResponse =
        FixerResponse.builder().base("USD").date("2025-02-04").rates(null).build();

    String description = fixerResponse.getDescription();

    String expectedDescription = "FixerResponse{base='USD', rates=null}";
    assertEquals(expectedDescription, description);
  }
}
