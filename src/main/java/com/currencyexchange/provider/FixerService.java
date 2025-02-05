package com.currencyexchange.provider;

import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FixerService implements ExchangeRateProvider {

  @Value("${fixer.api.key}")
  private String apiKey;

  @Value("${fixer.api.url}")
  private String apiUrl;

  private final RestTemplate restTemplate;

  public FixerService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public Map<String, Map<String, BigDecimal>> getLatestRates(Set<String> baseCurrencies) {
    Map<String, Map<String, BigDecimal>> result = new HashMap<>();

    for (String baseCurrency : baseCurrencies) {
      String url = String.format("%s?access_key=%s&base=%s", apiUrl, apiKey, baseCurrency);

      FixerResponse response = restTemplate.getForObject(url, FixerResponse.class);

      log.info("Fiex!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

      if (response != null && response.getRates() != null) {
        Map<String, Double> rates = response.getRates();
        Map<String, BigDecimal> decimalRates = new HashMap<>();

        for (Map.Entry<String, Double> entry : rates.entrySet()) {
          decimalRates.put(entry.getKey(), BigDecimal.valueOf(entry.getValue()));
        }

        result.put(baseCurrency, decimalRates);
      }
    }
    return result;
  }
}
