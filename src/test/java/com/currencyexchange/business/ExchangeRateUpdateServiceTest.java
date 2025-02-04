package com.currencyexchange.business;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.provider.ExchangeRateProvider;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExchangeRateUpdateServiceTest {

  @Mock
  private ExchangeRateProvider firstFetcher;

  @Mock
  private ExchangeRateRepositoryService currencyRateRepositoryService;

  private ExchangeRateUpdateService exchangeRateUpdateService;

  @BeforeEach
  void setUp() {
    List<ExchangeRateProvider> externalRateFetchers = List.of(firstFetcher);

    exchangeRateUpdateService =
        new ExchangeRateUpdateService(externalRateFetchers, currencyRateRepositoryService);
  }

  @Test
  void saveOrUpdateCurrencyRates_shouldSaveOrUpdateRates() {
    Map<String, Map<String, BigDecimal>> ratesFromApi1 =
        Map.of(
            "USD", Map.of("EUR", BigDecimal.valueOf(1.1)),
            "EUR", Map.of("USD", BigDecimal.valueOf(0.91)));
    when(firstFetcher.getLatestRates()).thenReturn(ratesFromApi1);

    exchangeRateUpdateService.refreshRates();

    verify(currencyRateRepositoryService).saveOrUpdateCurrencyRates(ratesFromApi1);
  }
}
