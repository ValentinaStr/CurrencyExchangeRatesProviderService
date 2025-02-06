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
  private ExchangeRateProvider exchangeRateProvider;

  @Mock
  private ExchangeRateRepositoryService exchangeRateRepositoryService;

  private ExchangeRateUpdateService exchangeRateUpdateService;

  @BeforeEach
  void setUp() {
    List<ExchangeRateProvider> externalRateFetchers = List.of(exchangeRateProvider);

    exchangeRateUpdateService =
        new ExchangeRateUpdateService(externalRateFetchers, exchangeRateRepositoryService);
  }

  @Test
  void saveOrUpdateCurrencyRates_shouldSaveOrUpdateRates() {
    Map<String, Map<String, BigDecimal>> ratesFromApi1 =
        Map.of(
            "USD", Map.of("EUR", BigDecimal.valueOf(1.1)),
            "EUR", Map.of("USD", BigDecimal.valueOf(0.91)));
    when(exchangeRateProvider.getLatestRates()).thenReturn(ratesFromApi1);

    exchangeRateUpdateService.updateCurrencyRatesInDatabase();

    verify(exchangeRateRepositoryService).saveOrUpdateCurrencyRates(ratesFromApi1);
  }
}
