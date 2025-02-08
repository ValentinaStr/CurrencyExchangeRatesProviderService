package com.currencyexchange.client;

import com.currencyexchange.business.ApiLogService;
import com.currencyexchange.dto.ExchangeRateResponseDto;
import com.currencyexchange.exception.ExchangeRateClientUnavailableException;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class FixerClient implements ExchangeRateClient {

  @Value("${fixer.api.key}")
  private String apiKey;

  @Value("${fixer.api.url}")
  private String apiUrl;

  private final RestTemplate restTemplate;
  private final ApiLogService apiLogService;

  @Override
  public ExchangeRateResponseDto getExchangeRate(Set<String> currency) {
    ExchangeRateResponseDto response = null;

    for (String baseCurrency : currency) {
      String url = String.format("%s/latest?access_key=%s&base=%s", apiUrl, apiKey, baseCurrency);
      log.info("Request URL: {}", url);

      try {
        response = restTemplate.getForObject(url, ExchangeRateResponseDto.class);
        apiLogService.logRequest(apiUrl, response);

      } catch (RestClientException e) {
        log.error("Failed to fetch exchange rates from {}: {}", url, e.getMessage());
        throw new ExchangeRateClientUnavailableException(
                "Failed to fetch exchange rates from: " + url, e);
      }
    }
    return response;
  }
}
