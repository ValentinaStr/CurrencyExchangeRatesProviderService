package com.currencyexchange.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.cache.ExchangeRateCacheService;
import com.currencyexchange.exception.RateNotFoundInCacheException;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

  @Mock
  private ExchangeRateCacheService exchangeRateCacheService;

  @Mock
  private ExchangeRateRepositoryService exchangeRateRepositoryService;

  @InjectMocks
  private ExchangeRateService exchangeRateService;

  @Test
  void getExchangeRates_shouldReturnRatesFromCache() {
    Map<String, BigDecimal> cachedRates = Map.of("USD", new BigDecimal("1.18"));
    when(exchangeRateCacheService.getExchangeRates("EUR")).thenReturn(cachedRates);

    Map<String, BigDecimal> result = exchangeRateService.getExchangeRates("EUR");

    assertEquals(cachedRates, result);
    verify(exchangeRateRepositoryService, never()).findRatesByBaseCurrency("EUR");
  }

  @Test
  void getExchangeRates_shouldFallBackToDatabaseOnCacheMiss() {
    Map<String, BigDecimal> dbRates = Map.of("USD", new BigDecimal("1.18"));
    when(exchangeRateCacheService.getExchangeRates("EUR"))
        .thenThrow(new RateNotFoundInCacheException("not found"));
    when(exchangeRateRepositoryService.findRatesByBaseCurrency("EUR")).thenReturn(dbRates);

    Map<String, BigDecimal> result = exchangeRateService.getExchangeRates("EUR");

    assertEquals(dbRates, result);
    verify(exchangeRateRepositoryService).findRatesByBaseCurrency("EUR");
  }

  @Test
  void getExchangeRates_shouldThrowWhenCacheMissAndDatabaseEmpty() {
    RateNotFoundInCacheException exception = new RateNotFoundInCacheException("not found");
    when(exchangeRateCacheService.getExchangeRates("EUR")).thenThrow(exception);
    when(exchangeRateRepositoryService.findRatesByBaseCurrency("EUR")).thenReturn(Map.of());

    assertThrows(RateNotFoundInCacheException.class,
        () -> exchangeRateService.getExchangeRates("EUR"));
  }
}
