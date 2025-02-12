package com.currencyexchange.client;

import com.currencyexchange.business.ApiLogService;
import com.currencyexchange.dto.ExchangeratesapiClientDto;
import com.currencyexchange.exception.ExchangeRateClientUnavailableException;
import com.currencyexchange.model.Rates;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class ExchangeratesapiClient implements ExchangeRateClient {

  @Value("${exchangeratesapi.api.key}")
  private String apiKey;

  @Value("${exchangeratesapi.api.url}")
  private String apiUrl;

  private final RestTemplate secureRestTemplate;
  private final ApiLogService apiLogService;

  @Override
  public Rates getExchangeRate(Set<String> baseCurrencies) {
    Rates rates = null;
    for (String baseCurrency : baseCurrencies) {
      String url =
          UriComponentsBuilder.fromUriString(apiUrl)
              .path("/latest")
              .queryParam("access_key", apiKey)
              .toUriString();
      log.info("Request URL: {}", url);

      try {
        ExchangeratesapiClientDto response =
            secureRestTemplate.getForObject(url, ExchangeratesapiClientDto.class);
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
