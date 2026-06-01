package com.currencyexchange.client;

import com.currencyexchange.business.ApiLogService;
import com.currencyexchange.dto.FixerDto;
import com.currencyexchange.exception.ExchangeRateClientUnavailableException;
import com.currencyexchange.mapper.ResponseModelMapper;
import com.currencyexchange.model.RatesModel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class FixerClient implements ExchangeRateClient {

  @Value("${fixer.api.key}")
  private String apiKey;

  @Value("${fixer.api.url}")
  private String apiUrl;

  private final RestTemplate restTemplate;
  private final ApiLogService apiLogService;
  private final ResponseModelMapper responseModelMapper;

  /**
   * Constructs a new FixerClient with the specified RestTemplate and ApiLogService.
   *
   * @param restTemplate the RestTemplate bean, qualified as "restTemplate"
   * @param apiLogService the service used for logging API requests and responses
   * @param responseModelMapper the mapper for converting DTOs to RatesModel
   */
  public FixerClient(
      @Qualifier("restTemplate") RestTemplate restTemplate,
      ApiLogService apiLogService,
      ResponseModelMapper responseModelMapper) {
    this.restTemplate = restTemplate;
    this.apiLogService = apiLogService;
    this.responseModelMapper = responseModelMapper;
  }

  @Override
  public RatesModel getExchangeRate(String baseCurrency) {
    log.debug("Fetching rates from Fixer for base: {}", baseCurrency);

    try {
      FixerDto response = restTemplate.getForObject(buildUrl(baseCurrency), FixerDto.class);
      if (response != null && response.success()) {
        RatesModel rates = responseModelMapper.fixerDtoToRatesModel(response);
        apiLogService.logRequest(apiUrl, rates);
        return rates;
      } else {
        log.warn("Fixer returned unsuccessful response for base: {}", baseCurrency);
        return null;
      }
    } catch (RestClientException e) {
      log.error("Failed to fetch exchange rates from Fixer for base: {}", baseCurrency, e);
      throw new ExchangeRateClientUnavailableException(
          "Failed to fetch exchange rates from Fixer", e);
    }
  }

  private String buildUrl(String baseCurrency) {
    return UriComponentsBuilder.fromUriString(apiUrl)
        .path("/latest")
        .queryParam("access_key", apiKey)
        .queryParam("base", baseCurrency)
        .toUriString();
  }
}
