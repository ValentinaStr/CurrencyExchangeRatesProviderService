package com.currencyexchange.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.currencyexchange.exception.RateNotFoundInCacheException;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExchangeRateCacheServiceTest {

  private ExchangeRateCacheService cacheService;

  @BeforeEach
  void setUp() {
    cacheService = new ExchangeRateCacheService();
  }

  @Test
  void updateAll_shouldStoreRatesInCache() {
    Map<String, BigDecimal> usdRates = Map.of("EUR", new BigDecimal("0.85"),
        "JPY", new BigDecimal("110.25"));

    cacheService.updateAll(Map.of("USD", usdRates));

    assertEquals(usdRates, cacheService.getExchangeRates("USD"));
  }

  @Test
  void updateAll_shouldOverwriteExistingRates() {
    cacheService.updateAll(Map.of("USD", Map.of("EUR", new BigDecimal("0.85"))));
    Map<String, BigDecimal> updatedRates = Map.of("EUR", new BigDecimal("0.90"));
    cacheService.updateAll(Map.of("USD", updatedRates));

    assertEquals(updatedRates, cacheService.getExchangeRates("USD"));
  }

  @Test
  void updateAll_shouldStoreMultipleCurrencies() {
    Map<String, BigDecimal> usdRates = Map.of("EUR", new BigDecimal("0.85"));
    Map<String, BigDecimal> gbpRates = Map.of("EUR", new BigDecimal("1.17"));

    cacheService.updateAll(Map.of("USD", usdRates, "GBP", gbpRates));

    assertEquals(usdRates, cacheService.getExchangeRates("USD"));
    assertEquals(gbpRates, cacheService.getExchangeRates("GBP"));
  }

  @Test
  void updateAll_shouldDoNothingWhenRatesEmpty() {
    cacheService.updateAll(Map.of());

    assertThrows(RateNotFoundInCacheException.class, () -> cacheService.getExchangeRates("USD"));
  }

  @Test
  void getExchangeRates_shouldThrowWhenCurrencyNotFound() {
    RateNotFoundInCacheException ex = assertThrows(
        RateNotFoundInCacheException.class,
        () -> cacheService.getExchangeRates("RUB"));

    assertEquals("Exchange rates for currency RUB not found in cache", ex.getMessage());
  }
}
