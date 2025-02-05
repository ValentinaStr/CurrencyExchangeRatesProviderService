package com.currencyexchange.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.currencyexchange.cache.ExchangeRateCacheService;
import com.currencyexchange.provider.ExchangeRateProvider;
import java.math.BigDecimal;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ExchangeRateUpdateServiceTest {

  @Mock
  private ExchangeRateProvider exchangeRateProvider;
  @Mock private CurrencyService currencyService;

  @Mock private ExchangeRateProvider externalRateProvider1;

  @Mock
  private ExchangeRateRepositoryService exchangeRateRepositoryService;
  @Mock private ExchangeRateProvider externalRateProvider2;

  @Mock private ExchangeRateRepositoryService currencyRateRepositoryService;

  @Mock private ExchangeRateCacheService currencyRateCacheService;

  @InjectMocks private ExchangeRateUpdateService exchangeRateUpdateService;

  @BeforeEach
  void setUp() {
    List<ExchangeRateProvider> externalRateFetchers = List.of(exchangeRateProvider);
  public void setUp() {
    List<ExchangeRateProvider> externalRateFetchers =
        List.of(externalRateProvider1, externalRateProvider2);

    exchangeRateUpdateService =
        new ExchangeRateUpdateService(externalRateFetchers, exchangeRateRepositoryService);
        new ExchangeRateUpdateService(
            currencyService,
            externalRateFetchers,
            currencyRateCacheService,
            currencyRateRepositoryService);
  }

  @Test
  public void testRefreshRates() {
    Set<String> currencies = new HashSet<>(List.of("USD"));
    when(currencyService.getAllCurrencies()).thenReturn(currencies);

    Map<String, Map<String, BigDecimal>> ratesFromApi = new HashMap<>();
    Map<String, BigDecimal> usdRates = new HashMap<>();
    usdRates.put("EUR", BigDecimal.valueOf(0.9));
    usdRates.put("GBP", BigDecimal.valueOf(0.8));
    ratesFromApi.put("USD", usdRates);

    when(externalRateProvider1.getLatestRates(currencies)).thenReturn(ratesFromApi);

    exchangeRateUpdateService.refreshRates();

    verify(externalRateProvider1).getLatestRates(currencies);
    verify(currencyRateCacheService).saveRatesToCache(ratesFromApi);

    verify(externalRateProvider1).getLatestRates(currencies);
    verify(currencyRateCacheService).saveRatesToCache(ratesFromApi);
  }

  @Test
  public void testRefreshRates_WithTwoProviders() {

    Set<String> currencies = new HashSet<>(List.of("EUR"));
    when(currencyService.getAllCurrencies()).thenReturn(currencies);

    Map<String, Map<String, BigDecimal>> ratesFromApi1 =
        Map.of(
            "USD", Map.of("EUR", BigDecimal.valueOf(1.1)),
            "EUR", Map.of("USD", BigDecimal.valueOf(0.91)));
    when(exchangeRateProvider.getLatestRates()).thenReturn(ratesFromApi1);
            "EUR",
            Map.of(
                "USD", BigDecimal.valueOf(0.9),
                "GBP", BigDecimal.valueOf(0.8)));

    Map<String, Map<String, BigDecimal>> ratesFromApi2 =
        Map.of(
            "EUR",
            Map.of(
                "USD", BigDecimal.valueOf(0.5),
                "GBP", BigDecimal.valueOf(0.9)));

    Map<String, Map<String, BigDecimal>> expectedRates =
        Map.of(
            "EUR",
            Map.of(
                "USD", BigDecimal.valueOf(0.9),
                "GBP", BigDecimal.valueOf(0.9)));
    when(externalRateProvider1.getLatestRates(currencies)).thenReturn(ratesFromApi1);
    when(externalRateProvider2.getLatestRates(currencies)).thenReturn(ratesFromApi2);

    exchangeRateUpdateService.updateCurrencyRatesInDatabase();

    verify(exchangeRateRepositoryService).saveOrUpdateCurrencyRates(ratesFromApi1);
    verify(externalRateProvider1).getLatestRates(currencies);
    verify(externalRateProvider2).getLatestRates(currencies);
    verify(currencyRateCacheService).saveRatesToCache(expectedRates);
    verify(currencyRateRepositoryService).saveOrUpdateCurrencyRates(expectedRates);
  }
}
