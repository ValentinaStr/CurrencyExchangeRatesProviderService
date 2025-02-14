package com.currencyexchange.business;

import com.currencyexchange.client.ExchangeRateClient;
import com.currencyexchange.model.Rates;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateService {
  private final CurrencyService currencyService;
  private final List<ExchangeRateClient> exchangeRateClients;

  /**
   * Refreshes and returns the best available exchange rates from API clients.
   *
   * @return a map containing the best exchange rates for each currency pair
   */
  public Map<String, Map<String, BigDecimal>> getRates() {
    Set<String> baseCurrencies = currencyService.getAllCurrencies();
    Map<String, Map<String, BigDecimal>> bestRates = new HashMap<>();

    for (ExchangeRateClient client : exchangeRateClients) {
      Rates ratesFromApi = client.getExchangeRate(baseCurrencies);

      if (ratesFromApi != null && ratesFromApi.rates() != null) {
        updateBestRates(bestRates, ratesFromApi);
      }
    }
    return bestRates;
  }

  private void updateBestRates(
      Map<String, Map<String, BigDecimal>> bestRates, Rates exchangeRateResponseDto) {
    String baseCurrency = exchangeRateResponseDto.base();
    bestRates.putIfAbsent(baseCurrency, new HashMap<>());

    for (Map.Entry<String, BigDecimal> entry : exchangeRateResponseDto.rates().entrySet()) {
      String targetCurrency = entry.getKey();
      BigDecimal newRate = entry.getValue();

      bestRates.get(baseCurrency).merge(targetCurrency, newRate, BigDecimal::max);
    }
  }
}
