package com.currencyexchange.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.currencyexchange.exception.RateNotFoundInCacheException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateCacheServiceTest {

  @InjectMocks
  private ExchangeRateCacheService exchangeRateCacheService;

  @Test
  void testGetExchangeRatesCache_currencyExists() throws RateNotFoundInCacheException {
    exchangeRateCacheService.getExchangeRate("EUR");

    Double exchangeRate = exchangeRateCacheService.getExchangeRate("EUR");

    assertEquals(1, exchangeRate, "The exchange rate should match the one in cache");
  }

  @Test
  void testGetExchangeRatesCache_currencyNotExist() {
    var exception = assertThrows(RateNotFoundInCacheException.class, () -> {
      exchangeRateCacheService.getExchangeRate("RUB");
    });

    assertEquals("Exchange rate for RUB not found in cache", exception.getMessage());
  }
}
