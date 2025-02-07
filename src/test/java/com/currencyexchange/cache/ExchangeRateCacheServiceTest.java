package com.currencyexchange.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.currencyexchange.exception.RateNotFoundInCacheException;
import java.math.BigDecimal;
import java.util.HashMap;
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
  void testSave_shouldUpdateCache() {
    Map<String, Map<String, BigDecimal>> rates = new HashMap<>();
    Map<String, BigDecimal> usdRates = new HashMap<>();
    usdRates.put("EUR", new BigDecimal("0.85"));
    usdRates.put("JPY", new BigDecimal("110.25"));
    rates.put("USD", usdRates);
    exchangeRateCacheService.save(rates);

    assertEquals(usdRates, exchangeRateCacheService.getExchangeRates("USD"));
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
}
