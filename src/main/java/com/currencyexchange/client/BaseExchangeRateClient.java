package com.currencyexchange.client;

import com.currencyexchange.business.ApiLogService;
import com.currencyexchange.dto.ExchangeRateResponseDto;
import com.currencyexchange.exception.ExchangeRateClientUnavailableException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Component
public abstract class BaseExchangeRateClient implements ExchangeRateClient {

  private final ApiLogService apiLogService;

  /**
   * Constructs the API URL based on the base currency.
   *
   * @param baseCurrency the base currency for which rates are requested.
   * @return the API URL.
   */
  protected abstract String getUrl(String baseCurrency);

  /**
   * Returns the {@link RestTemplate} used for making API requests.
   *
   * @return a {@link RestTemplate} instance.
   */
  protected abstract RestTemplate getRestTemplate();

  @Override
  public ExchangeRateResponseDto getExchangeRate(Set<String> currencies) {
    ExchangeRateResponseDto response =
        new ExchangeRateResponseDto(false, 0L, "", new HashMap<>());
    for (String baseCurrency : currencies) {
      String url = getUrl(baseCurrency);
      log.info("Request URL: {}", url);

      try {
        var rest = getRestTemplate();
        response = rest.getForObject(url, ExchangeRateResponseDto.class);
        apiLogService.logRequest(url, response);
      } catch (RestClientException e) {
        log.info("Failed to fetch exchange rates from {}: {}", url, e.getMessage());
        throw new ExchangeRateClientUnavailableException(
            "Failed to fetch exchange rates from: " + url, e);
      }
    }
    return response;
  }
}
