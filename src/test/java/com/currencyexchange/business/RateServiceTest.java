package com.currencyexchange.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.client.ExchangeRateClient;
import com.currencyexchange.dto.ExchangeRateResponseDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

  @InjectMocks
  private RateService rateService;

  @BeforeEach
  void setUp() {
    List<ExchangeRateClient> exchangeRateClients = List.of(client1, client2);
    rateService = new RateService(currencyService, exchangeRateClients);
  }

  @Test
  void refreshRates_shouldReturnBestExchangeRates() {
    Set<String> currencies = Set.of("USD", "EUR");
    when(currencyService.getAllCurrencies()).thenReturn(currencies);
    ExchangeRateResponseDto ratesFromClient1 =
        new ExchangeRateResponseDto(
            true,
            1707302400L,
            "USD",
            Map.of(
                "EUR", new BigDecimal("0.90"),
                "GBP", new BigDecimal("0.75")));

    ExchangeRateResponseDto ratesFromClient2 =
        new ExchangeRateResponseDto(
            true,
            1707302400L,
            "USD",
            Map.of(
                "EUR", new BigDecimal("0.92"),
                "GBP", new BigDecimal("0.73")));

    when(client1.getExchangeRate(currencies)).thenReturn(ratesFromClient1);
    when(client2.getExchangeRate(currencies)).thenReturn(ratesFromClient2);

    Map<String, Map<String, BigDecimal>> bestRates = rateService.refreshRates();

    assertNotNull(bestRates);
    assertEquals(new BigDecimal("0.92"), bestRates.get("USD").get("EUR"));
    assertEquals(new BigDecimal("0.75"), bestRates.get("USD").get("GBP"));
    verify(client1).getExchangeRate(currencies);
    verify(client2).getExchangeRate(currencies);
  }

  @Test
  void refreshRates_shouldHandleNullResponses() {
    Set<String> currencies = Set.of("USD");
    when(currencyService.getAllCurrencies()).thenReturn(currencies);
    when(client1.getExchangeRate(currencies))
        .thenReturn(new ExchangeRateResponseDto(true, 1707302400L, "USD", Map.of()));

    when(client2.getExchangeRate(currencies))
        .thenReturn(
            new ExchangeRateResponseDto(
                true, 1707302400L, "USD", Map.of("EUR", new BigDecimal("0.91"))));

    Map<String, Map<String, BigDecimal>> bestRates = rateService.refreshRates();

    assertNotNull(bestRates);
    assertTrue(bestRates.containsKey("USD"));
    assertEquals(1, bestRates.get("USD").size());
    assertEquals(new BigDecimal("0.91"), bestRates.get("USD").get("EUR"));
  }

  @Test
  void refreshRates_shouldSkipNullOrEmptyResponses() {
    Set<String> currencies = Set.of("USD");
    when(currencyService.getAllCurrencies()).thenReturn(currencies);
    when(client1.getExchangeRate(currencies)).thenReturn(null);

    Map<String, Map<String, BigDecimal>> bestRates = rateService.refreshRates();

    assertNotNull(bestRates);
    assertTrue(bestRates.isEmpty());
    verify(client1).getExchangeRate(currencies);
  }

  @Test
  void refreshRates_shouldHandleNullRatesMap() {
    Set<String> currencies = Set.of("USD");
    when(currencyService.getAllCurrencies()).thenReturn(currencies);

    ExchangeRateResponseDto response = new ExchangeRateResponseDto(true, 1707302400L, "USD", null);
    when(client1.getExchangeRate(currencies)).thenReturn(response);

    Map<String, Map<String, BigDecimal>> bestRates = rateService.refreshRates();

    assertNotNull(bestRates);
    assertTrue(bestRates.isEmpty());
  }
}
