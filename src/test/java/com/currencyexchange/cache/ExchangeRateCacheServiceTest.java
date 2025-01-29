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
  void getExchangeRatesCache_shouldReturnRatesWhenCurrencyExists()
      throws RateNotFoundInCacheException {
    Map<String, BigDecimal> expectedRates = Map.of(
        "USD", new BigDecimal("0.87"),
        "GBP", new BigDecimal("0.85")
    );

    Map<String, BigDecimal> actualRates = exchangeRateCacheService.getExchangeRates("EUR");

    assertEquals(expectedRates, actualRates, "Rates map for EUR should match expected values");
  }

  @Test
  void getExchangeRatesCache_shouldThrowExceptionWhenCurrencyNotExist() {

    var exception = assertThrows(RateNotFoundInCacheException.class, () -> {
      exchangeRateCacheService.getExchangeRates("RUB");
    });

    assertEquals("Exchange rates for currency RUB not found in cache", exception.getMessage());
  }
}
