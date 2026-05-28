package com.currencyexchange.business;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.currencyexchange.cache.ExchangeRateCacheService;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExchangeRateUpdateServiceTest {

  @Mock
  private RateService rateService;

  @Mock
  private CurrencyService currencyService;

  @Mock
  private ExchangeRateRepositoryService exchangeRateRepositoryService;

  @Mock
  private ExchangeRateCacheService exchangeRateCacheService;

  @InjectMocks
  private ExchangeRateUpdateService exchangeRateUpdateService;

  @Test
  void refreshRates_shouldSaveAndCacheWhenRatesAvailable() {
    Map<String, Map<String, BigDecimal>> rates = Map.of(
        "USD", Map.of("EUR", BigDecimal.valueOf(0.9), "GBP", BigDecimal.valueOf(0.8)));
    when(rateService.getRates()).thenReturn(rates);

    exchangeRateUpdateService.refreshRates();

    verify(rateService).getRates();
    verify(exchangeRateRepositoryService).saveOrUpdateCurrencyRates(rates);
    verify(exchangeRateCacheService).updateAll(rates);
  }

  @Test
  void refreshRates_shouldSkipSaveAndCacheWhenRatesEmpty() {
    when(rateService.getRates()).thenReturn(Map.of());

    exchangeRateUpdateService.refreshRates();

    verify(rateService).getRates();
    verifyNoInteractions(exchangeRateRepositoryService);
    verifyNoInteractions(exchangeRateCacheService);
  }
}
