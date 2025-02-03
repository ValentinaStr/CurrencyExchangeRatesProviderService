package com.currencyexchange.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.currencyexchange.exception.RateNotFoundInCacheException;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExchangeRateCacheServiceTest {

  @InjectMocks
  private ExchangeRateCacheService exchangeRateCacheService;

  @Test
  void getExchangeRatesCache_shouldReturnRatesWhenCurrencyExists() {
    Map<String, BigDecimal> expectedRates =
        Map.of(
            "USD", new BigDecimal("0.87"),
            "GBP", new BigDecimal("0.85"));

    Map<String, BigDecimal> actualRates = exchangeRateCacheService.getExchangeRates("EUR");

    assertEquals(expectedRates, actualRates, "Rates map for EUR should match expected values");
  }

  @Test
  void getExchangeRatesCache_shouldThrowExceptionWhenCurrencyNotExist() {

    var exception =
        assertThrows(
            RateNotFoundInCacheException.class,
            () -> {
              exchangeRateCacheService.getExchangeRates("RUB");
            });

    assertEquals("Exchange rates for currency RUB not found in cache", exception.getMessage());
  }

  @Test
  void saveRatesToCache_shouldSaveExchangeRatesToCache() {
    Map<String, Map<String, BigDecimal>> newRates =
        Map.of(
            "EUR", Map.of("USD", new BigDecimal("1.2"), "GBP", new BigDecimal("0.9")),
            "USD", Map.of("EUR", new BigDecimal("0.8"), "GBP", new BigDecimal("0.75")));

    exchangeRateCacheService.saveRatesToCache(newRates);

    assertEquals(
        new BigDecimal("1.2"), exchangeRateCacheService.getExchangeRates("EUR").get("USD"));
    assertEquals(
        new BigDecimal("0.9"), exchangeRateCacheService.getExchangeRates("EUR").get("GBP"));
    assertEquals(
        new BigDecimal("0.8"), exchangeRateCacheService.getExchangeRates("USD").get("EUR"));
    assertEquals(
        new BigDecimal("0.75"), exchangeRateCacheService.getExchangeRates("USD").get("GBP"));
  }
}
