package com.currencyexchange.client;

import com.currencyexchange.business.ApiLogService;
import com.currencyexchange.dto.FixerDto;
import com.currencyexchange.exception.ExchangeRateClientUnavailableException;
import com.currencyexchange.model.RatesModel;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@Getter
public class FixerClient implements ExchangeRateClient {

  @Value("${fixer.api.key}")
  private String apiKey;

  @Value("${fixer.api.url}")
  private String apiUrl;

  private final RestTemplate restTemplate;
  private final ApiLogService apiLogService;

  /**
   * Constructs a new FixerClient with the specified RestTemplate and ApiLogService.
   *
   * @param restTemplate the RestTemplate bean, qualified as "getRestTemplate"
   * @param apiLogService the service used for logging API requests and responses
   */
  public FixerClient(
      @Qualifier("restTemplate") RestTemplate restTemplate, ApiLogService apiLogService) {
    this.restTemplate = restTemplate;
    this.apiLogService = apiLogService;
  }

  @Override
  public RatesModel getExchangeRate(Set<String> currency) {
    RatesModel rates = null;
    for (String baseCurrency : currency) {
      String url =
          UriComponentsBuilder.fromUriString(apiUrl)
              .path("/latest")
              .queryParam("access_key", apiKey)
              .queryParam("base", baseCurrency)
              .toUriString();
      log.info("Request URL: {}", url);

      try {
        FixerDto response = restTemplate.getForObject(url, FixerDto.class);
        if (response != null) {
          rates = response.toRates();
          apiLogService.logRequest(apiUrl, rates);
        }

      } catch (RestClientException e) {
        log.error("Failed to fetch exchange rates from {}: {}", url, e.getMessage());
        throw new ExchangeRateClientUnavailableException(
            "Failed to fetch exchange rates from: " + url, e);
      }
    }
    return rates;
  }
}
