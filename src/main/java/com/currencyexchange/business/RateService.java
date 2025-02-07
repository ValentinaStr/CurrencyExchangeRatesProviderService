package com.currencyexchange.business;


import com.currencyexchange.client.ExchangeRateClient;
import com.currencyexchange.dto.ExchangeRateResponseDto;

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
public class RateService
{
  private final CurrencyService currencyService;
  private final List<ExchangeRateClient> exchangeRateClients;

  public Map<String, Map<String, BigDecimal>> refreshRates() {

    Set<String> baseCurrencies = currencyService.getAllCurrencies();
    Map<String, Map<String, BigDecimal>> bestRates = new HashMap<>();

    for (ExchangeRateClient client : exchangeRateClients) {
      ExchangeRateResponseDto ratesFromApi = client.getExchangeRate(baseCurrencies);

      if (ratesFromApi != null && ratesFromApi.getRates() != null) {
        updateBestRates(bestRates, ratesFromApi);
      }
    }
    return  bestRates;
  }

  private void updateBestRates(
      Map<String, Map<String, BigDecimal>> bestRates, ExchangeRateResponseDto ratesFromApi) {

    String baseCurrency = ratesFromApi.getBase();

    bestRates.putIfAbsent(baseCurrency, new HashMap<>());

    for (Map.Entry<String, BigDecimal> entry : ratesFromApi.getRates().entrySet()) {
      String targetCurrency = entry.getKey();
      BigDecimal newRate = entry.getValue();

      bestRates.get(baseCurrency).merge(targetCurrency, newRate, BigDecimal::max);
    }
  }
}
