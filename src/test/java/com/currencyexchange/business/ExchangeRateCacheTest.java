package com.currencyexchange.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.currencyexchange.exception.RateNotFoundInCacheException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateCacheTest {

  @InjectMocks
  private ExchangeRateCacheService exchangeRateCacheService;


  @Test
  void testGetExchangeRatesCache_currencyExists() throws RateNotFoundInCacheException {
    String currency = "USD";
    double expectedRate = 1.25;
    exchangeRateCacheService.addExchangeRate(currency, expectedRate);

    Double exchangeRate = exchangeRateCacheService.getExchangeRate(currency);
    assertEquals(expectedRate, exchangeRate, "The exchange rate should match the one in cache");
  }

  @Test
  void testGetExchangeRatesCache_currencyNotExist() {
    exchangeRateCacheService.clearCache();

    var exception = assertThrows(RateNotFoundInCacheException.class, () -> {
      exchangeRateCacheService.getExchangeRate("GBP");
    });

    assertEquals("Exchange rate for GBP not found in cache", exception.getMessage());
  }
}
