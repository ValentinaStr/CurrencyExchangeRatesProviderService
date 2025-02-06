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
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenExchangeRatesService implements ExchangeRateProvider {

  @Value("${openexchangerates.api.key}")
  private String apiKey;

  @Value("${openexchangerates.api.url}")
  private String apiUrl;

  private final RestTemplate secureRestTemplate;
  private final ApiLogService apiLogService;

  @Override
  public Map<String, Map<String, BigDecimal>> getLatestRates(Set<String> baseCurrencies) {
    Map<String, Map<String, BigDecimal>> result = new HashMap<>();

    for (String baseCurrency : baseCurrencies) {

      String url =
          UriComponentsBuilder.fromHttpUrl(apiUrl)
              .path("/latest.json")
              .queryParam("app_id", apiKey)
              .toUriString();

      log.info("Request URL: {}", url);

      OpenExchangeRatesResponse response =
              secureRestTemplate.getForObject(url, OpenExchangeRatesResponse.class);

      apiLogService.saveApiLog(url, response);

      if (response != null && response.getBase() != null && response.getRates() != null) {
        result.put(response.getBase(), convertRatesToBigDecimal(response.getRates()));
      }
    }

    return result;
  }

  private Map<String, BigDecimal> convertRatesToBigDecimal(Map<String, Double> rates) {
    Map<String, BigDecimal> decimalRates = new HashMap<>();

    for (Map.Entry<String, Double> entry : rates.entrySet()) {
      decimalRates.put(entry.getKey(), BigDecimal.valueOf(entry.getValue()));
    }

    return decimalRates;
  }
}
