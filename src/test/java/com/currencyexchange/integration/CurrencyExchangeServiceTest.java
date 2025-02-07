package com.currencyexchange.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.currencyexchange.business.RateService;
import com.currencyexchange.config.TestContainerConfig;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
public class CurrencyExchangeServiceTest extends TestContainerConfig {

  @Autowired
  private RateService exchangeRateUpdateService;

  @Autowired
  private JdbcTemplate jdbcTemplate;



  @RegisterExtension
  static WireMockExtension wireMockExtension =
      WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();

  /**
   * Registers the WireMock base URL as a dynamic property for the application context.
   *
   * @param registry the {@link DynamicPropertyRegistry} used to register the property.
   */
  @DynamicPropertySource
  public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
    registry.add("${fixer.api.url}", wireMockExtension::baseUrl);
  }

  @Test
  public void getLatestRates_shouldReturnRates() {

    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "EUR");

    String mockResponse =
        "{"
            + "\"success\": true,"
            + "\"timestamp\": 1519296206,"
            + "\"base\": \"EUR\","
            + "\"date\": \"2025-02-04\","
            + "\"rates\": {"
            + "\"AUD\": 1.566015,"
            + "\"CAD\": 1.560132,"
            + "\"JPY\": 132.360679,"
            + "\"USD\": 1.23396"
            + "}"
            + "}";

    String url = "/latest?access_key=751c8d53f0ffb020e36053b7c5a36a2c&base=EUR";

    wireMockExtension.stubFor(
        get(url)
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody(mockResponse)
                    .withHeader("Content-Type", "application/json")));

    Map<String, Map<String, BigDecimal>> exchangeRates = exchangeRateUpdateService.refreshRates();

    assertTrue(exchangeRates.containsKey("EUR"));

    assertAll(
        "Exchange rates for EUR",
        () -> assertEquals(new BigDecimal("1.23396"), exchangeRates.get("EUR").get("USD")),
        () -> assertEquals(new BigDecimal("1.566015"), exchangeRates.get("EUR").get("AUD")),
        () -> assertEquals(new BigDecimal("132.360679"), exchangeRates.get("EUR").get("JPY")),
        () -> assertEquals(new BigDecimal("1.560132"), exchangeRates.get("EUR").get("CAD")));
  }
}
