package com.currencyexchange.client;

import com.currencyexchange.business.ApiLogService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@Getter
public class ExchangeratesapiClient extends BaseExchangeRateClient {

  @Value("${exchangeratesapi.api.key}")
  private String apiKey;

  @Value("${exchangeratesapi.api.url}")
  private String apiUrl;

  private final RestTemplate secureRestTemplate;

  /**
   * Constructs an instance of {@link ExchangeratesapiClient}.
   *
   * @param apiLogService the {@link ApiLogService} used to log API requests.
   * @param secureRestTemplate the {@link RestTemplate} used to make secure HTTP requests.
   */
  public ExchangeratesapiClient(ApiLogService apiLogService, RestTemplate secureRestTemplate) {
    super(apiLogService);
    this.secureRestTemplate = secureRestTemplate;
  }

  @Override
  protected String getUrl(String baseCurrency) {
    return UriComponentsBuilder.fromUriString(apiUrl)
        .path("/latest")
        .queryParam("access_key", apiKey)
        .toUriString();
  }

  @Override
  protected RestTemplate getRestTemplate() {
    return secureRestTemplate;
  }
}
