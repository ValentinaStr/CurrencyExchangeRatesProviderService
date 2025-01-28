package com.currencyexchange.business;

import com.currencyexchange.exception.RateNotFoundInCacheException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateCacheServiceTest {

  @InjectMocks
  private ExchangeRateCacheService exchangeRateCacheService;

  @Test
  void testGetExchangeRatesCache_currencyExists() throws RateNotFoundInCacheException {
    Map<String, Double> expectedRates = Map.of(
        "EUR", 1.0,
        "USD", 1.087,
        "GBP", 0.85,
        "JPY", 141.53
    );

    Map<String, Double> actualRates = exchangeRateCacheService.getExchangeRate("EUR");

    assertEquals(expectedRates, actualRates, "The exchange rates map for EUR should match the expected values");
  }

  @Test
  void testGetExchangeRatesCache_currencyNotExist() {

    var exception = assertThrows(RateNotFoundInCacheException.class, () -> {
      exchangeRateCacheService.getExchangeRate("RUB");
    });

    assertEquals("Exchange rates for currency RUB not found in cache", exception.getMessage());
  }

}
