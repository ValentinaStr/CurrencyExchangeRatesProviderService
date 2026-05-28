package com.currencyexchange.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.client.ExchangeRateClient;
import com.currencyexchange.model.RatesModel;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RateServiceTest {

  @Mock
  private CurrencyService currencyService;

  @Mock
  private ExchangeRateClient client1;

  @Mock
  private ExchangeRateClient client2;

  private RateService rateService;

  @BeforeEach
  void setUp() {
    rateService = new RateService(currencyService, List.of(client1, client2));
  }

  @Test
  void getRates_shouldReturnBestExchangeRates() {
    Set<String> currencies = Set.of("USD", "EUR");
    when(currencyService.getAllCurrencies()).thenReturn(currencies);
    when(client1.getExchangeRate(currencies)).thenReturn(RatesModel.builder()
        .base("USD").timestamp(1707302400L)
        .rates(Map.of("EUR", new BigDecimal("0.90"), "GBP", new BigDecimal("0.73")))
        .build());
    when(client2.getExchangeRate(currencies)).thenReturn(RatesModel.builder()
        .base("USD").timestamp(1707302400L)
        .rates(Map.of("EUR", new BigDecimal("0.92"), "GBP", new BigDecimal("0.75")))
        .build());

    Map<String, Map<String, BigDecimal>> bestRates = rateService.getRates();

    assertEquals(new BigDecimal("0.92"), bestRates.get("USD").get("EUR"));
    assertEquals(new BigDecimal("0.75"), bestRates.get("USD").get("GBP"));
    verify(client1).getExchangeRate(currencies);
    verify(client2).getExchangeRate(currencies);
  }

  @Test
  void getRates_shouldHandleEmptyRatesFromOneClient() {
    Set<String> currencies = Set.of("USD");
    when(currencyService.getAllCurrencies()).thenReturn(currencies);
    when(client1.getExchangeRate(currencies))
        .thenReturn(new RatesModel(1707302400L, "USD", Map.of()));
    when(client2.getExchangeRate(currencies))
        .thenReturn(new RatesModel(1707302400L, "USD", Map.of("EUR", new BigDecimal("0.91"))));

    Map<String, Map<String, BigDecimal>> bestRates = rateService.getRates();

    assertTrue(bestRates.containsKey("USD"));
    assertEquals(1, bestRates.get("USD").size());
    assertEquals(new BigDecimal("0.91"), bestRates.get("USD").get("EUR"));
  }

  @Test
  void getRates_shouldSkipNullResponses() {
    Set<String> currencies = Set.of("USD");
    when(currencyService.getAllCurrencies()).thenReturn(currencies);
    when(client1.getExchangeRate(currencies)).thenReturn(null);
    when(client2.getExchangeRate(currencies)).thenReturn(null);

    Map<String, Map<String, BigDecimal>> bestRates = rateService.getRates();

    assertTrue(bestRates.isEmpty());
  }

  @Test
  void getRates_shouldSkipNullRatesMap() {
    Set<String> currencies = Set.of("USD");
    when(currencyService.getAllCurrencies()).thenReturn(currencies);
    when(client1.getExchangeRate(currencies))
        .thenReturn(new RatesModel(1707302400L, "USD", null));
    when(client2.getExchangeRate(currencies)).thenReturn(null);

    Map<String, Map<String, BigDecimal>> bestRates = rateService.getRates();

    assertTrue(bestRates.isEmpty());
  }
}
