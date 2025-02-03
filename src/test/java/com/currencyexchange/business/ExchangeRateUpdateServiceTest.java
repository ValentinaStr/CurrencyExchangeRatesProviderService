package com.currencyexchange.business;

import static org.mockito.Mockito.*;

import com.currencyexchange.cache.ExchangeRateCacheService;
import com.currencyexchange.repository.ExchangeRateFetcher;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExchangeRateUpdateServiceTest {

  @Mock
  private ExchangeRateFetcher firstFetcher;

  @Mock
  private ExchangeRateCacheService currencyRateCacheService;

  @Mock
  private ExchangeRateRepositoryService currencyRateRepositoryService;

  @InjectMocks
  private ExchangeRateUpdateService exchangeRateUpdateService;

  @BeforeEach
  void setUp() {
    List<ExchangeRateFetcher> externalRateFetchers = List.of(firstFetcher);

    exchangeRateUpdateService =
        new ExchangeRateUpdateService(
            externalRateFetchers, currencyRateCacheService, currencyRateRepositoryService);
  }

  @Test
  void testRefreshRates() {
    Map<String, Map<String, BigDecimal>> ratesFromApi1 =
        Map.of(
            "USD", Map.of("EUR", BigDecimal.valueOf(1.1)),
            "EUR", Map.of("USD", BigDecimal.valueOf(0.91)));
    when(firstFetcher.getLatestRates()).thenReturn(ratesFromApi1);

    exchangeRateUpdateService.refreshRates();

    verify(currencyRateRepositoryService).saveOrUpdateCurrencyRates(ratesFromApi1);
    verify(currencyRateCacheService).saveRatesToCache(ratesFromApi1);
  }


}
