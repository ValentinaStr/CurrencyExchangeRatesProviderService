package com.currencyexchange.business;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.cache.ExchangeRateCacheService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExchangeRateUpdateServiceTest {

  @Mock
  private CurrencyService currencyService;

  @Mock
  private RateService rateService;

  @Mock
  private ExchangeRateRepositoryService currencyRateRepositoryService;

  @Mock
  private ExchangeRateCacheService currencyRateCacheService;

  @InjectMocks
  private ExchangeRateUpdateService exchangeRateUpdateService;

  @Test
  public void testRefreshRates() {
    Map<String, Map<String, BigDecimal>> ratesFromApi = new HashMap<>();
    Map<String, BigDecimal> usdRates = new HashMap<>();
    usdRates.put("EUR", BigDecimal.valueOf(0.9));
    usdRates.put("GBP", BigDecimal.valueOf(0.8));
    ratesFromApi.put("USD", usdRates);
    when(rateService.getRates()).thenReturn(ratesFromApi);

    exchangeRateUpdateService.refreshRates();

    verify(rateService).getRates();
  }
}
