package com.currencyexchange.provider;

import com.currencyexchange.business.ApiLogService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FixerService implements ExchangeRateProvider {

  @Value("${fixer.api.key}")
  private String apiKey;

  @Value("${fixer.api.url}")
  private String apiUrl;

  private final RestTemplate restTemplate;
  private final ApiLogService apiLogService;

  @Override
  public Map<String, Map<String, BigDecimal>> getLatestRates(Set<String> baseCurrencies) {
    Map<String, Map<String, BigDecimal>> result = new HashMap<>();

    for (String baseCurrency : baseCurrencies) {
      String url = String.format("%s/latest?access_key=%s&base=%s", apiUrl, apiKey, baseCurrency);
      log.info("Request URL: {}", url);

      FixerResponse response = restTemplate.getForObject(url, FixerResponse.class);

      apiLogService.saveApiLog(apiUrl, response);

      if (response.getBase() != null && response.getRates() != null) {
        result.put(response.getBase(), response.getRates());
      }
    }

    return result;
  }
}
