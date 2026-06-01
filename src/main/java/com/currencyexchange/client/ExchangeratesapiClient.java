package com.currencyexchange.client;

import com.currencyexchange.business.ApiLogService;
import com.currencyexchange.dto.ExchangeratesapiClientDto;
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
public class ExchangeratesapiClient implements ExchangeRateClient {

  @Value("${exchangeratesapi.api.key}")
  private String apiKey;

  @Value("${exchangeratesapi.api.url}")
  private String apiUrl;

  private final RestTemplate restTemplate;
  private final ApiLogService apiLogService;
  private final ResponseModelMapper responseModelMapper;

  public ExchangeratesapiClient(
      @Qualifier("restTemplate") RestTemplate restTemplate,
      ApiLogService apiLogService,
      ResponseModelMapper responseModelMapper) {
    this.restTemplate = restTemplate;
    this.apiLogService = apiLogService;
    this.responseModelMapper = responseModelMapper;
  }

  @Override
  public RatesModel getExchangeRate(String baseCurrency) {
    log.debug("Fetching rates from Exchangeratesapi for base: {}", baseCurrency);

    try {
      ExchangeratesapiClientDto response =
          restTemplate.getForObject(buildUrl(), ExchangeratesapiClientDto.class);
      if (response != null && response.success()) {
        RatesModel rates = responseModelMapper.exchangeratesDtoToRatesModel(response);
        apiLogService.logRequest(apiUrl, rates);
        return rates;
      } else {
        log.warn("Exchangeratesapi returned unsuccessful response for base: {}", baseCurrency);
        return null;
      }
    } catch (RestClientException e) {
      log.error("Failed to fetch exchange rates from Exchangeratesapi for base: {}", baseCurrency, e);
      throw new ExchangeRateClientUnavailableException(
          "Failed to fetch exchange rates from Exchangeratesapi", e);
    }
  }

  private String buildUrl() {
    return UriComponentsBuilder.fromUriString(apiUrl)
        .path("/latest")
        .queryParam("access_key", apiKey)
        .toUriString();
  }
}
