package com.currencyexchange.client;

import com.currencyexchange.business.ApiLogService;
import com.currencyexchange.dto.ExchangeRateResponseDto;
import com.currencyexchange.exception.ExchangeRateClientUnavailableException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
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
public class OpenExchangeRatesClient implements ExchangeRateClient {

  @Value("${openexchangerates.api.key}")
  private String apiKey;

  @Value("${openexchangerates.api.url}")
  private String apiUrl;

  private final RestTemplate secureRestTemplate;
  private final ApiLogService apiLogService;

  @Override
  public ExchangeRateResponseDto getExchangeRate(Set<String> baseCurrencies) {
    ExchangeRateResponseDto response = null;

    for (String baseCurrency : baseCurrencies) {

      String url =
          UriComponentsBuilder.fromHttpUrl(apiUrl)
              .path("/latest.json")
              .queryParam("app_id", apiKey)
              .toUriString();

      log.info("Request URL: {}", url);

      try {
        response = secureRestTemplate.getForObject(url, ExchangeRateResponseDto.class);
        apiLogService.logRequest(apiUrl, response);

      } catch (RestClientException e) {
        log.error("Failed to fetch exchange rates from {}: {}", url, e.getMessage());
        throw new ExchangeRateClientUnavailableException(
            "Failed to fetch exchange rates from: " + url);
      }
    }
    return response;
  }
}
