package com.currencyexchange.client;

import com.currencyexchange.business.ApiLogService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Getter
public class FixerClient extends BaseExchangeRateClient {

  @Value("${fixer.api.key}")
  private String apiKey;

  @Value("${fixer.api.url}")
  private String apiUrl;

  private final RestTemplate restTemplate;

  /**
   * Constructs an instance of {@link FixerClient}.
   *
   * @param apiLogService the {@link ApiLogService} used to log API requests.
   */
  public FixerClient(
      ApiLogService apiLogService, @Qualifier("getRestTemplate") RestTemplate restTemplate) {
    super(apiLogService);
    this.restTemplate = restTemplate;
  }

  @Override
  public String getUrl(String baseCurrency) {
    return UriComponentsBuilder.fromUriString(apiUrl)
        .path("/latest")
        .queryParam("access_key", apiKey)
        .queryParam("base", baseCurrency)
        .toUriString();
  }

  @Override
  public RestTemplate getRestTemplate() {
    return restTemplate;
  }
}
