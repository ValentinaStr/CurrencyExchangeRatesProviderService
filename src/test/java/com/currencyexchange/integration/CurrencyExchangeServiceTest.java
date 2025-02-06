package com.currencyexchange.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

import com.currencyexchange.business.ExchangeRateUpdateService;
import com.currencyexchange.config.TestContainerConfig;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
public class CurrencyExchangeServiceTest extends TestContainerConfig
{

  @Autowired
  private ExchangeRateUpdateService exchangeRateUpdateService;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @RegisterExtension
  static WireMockExtension wireMockExtension =
      WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();

  @DynamicPropertySource
  public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
    registry.add("${fixer.api.url}", wireMockExtension::baseUrl);
  }

  @Test
  public void getLatestRates_ShouldReturnRates() {

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

    String url = "/latest?access_key=71eb9f9d589f4b2c311dbda4dfac5bc3&base=EUR";

    wireMockExtension.stubFor(
        get(url)
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody(mockResponse)
                    .withHeader("Content-Type", "application/json")));

    var exchangeRates = exchangeRateUpdateService.refreshRates();

    assertTrue(exchangeRates.containsKey("EUR"));

    assertAll(
        "Exchange rates for EUR",
        () -> assertEquals(new BigDecimal("1.23396"), exchangeRates.get("EUR").get("USD")),
        () -> assertEquals(new BigDecimal("1.566015"), exchangeRates.get("EUR").get("AUD")),
        () -> assertEquals(new BigDecimal("132.360679"), exchangeRates.get("EUR").get("JPY")),
        () -> assertEquals(new BigDecimal("1.560132"), exchangeRates.get("EUR").get("CAD")));
  }
}
