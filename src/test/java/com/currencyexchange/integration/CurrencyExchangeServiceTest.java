package com.currencyexchange.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.currencyexchange.business.ExchangeRateUpdateService;
import com.currencyexchange.cache.ExchangeRateCacheService;
import com.currencyexchange.config.TestContainerConfig;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
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
  private ExchangeRateUpdateService exchangeRateUpdateService;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private ExchangeRateCacheService exchangeRateCacheService;

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

  @BeforeEach
  void setUp() {

    jdbcTemplate.update("DELETE FROM api_logs");
    jdbcTemplate.update("DELETE FROM exchange_rates");
    jdbcTemplate.update("DELETE FROM currencies");
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
  }

  @Test
  public void getLatestRates_shouldExistInCache() {

    Map<String, BigDecimal> rates = new HashMap<>();
    rates.put("AUD", new BigDecimal("1.566015"));
    rates.put("CAD", new BigDecimal("1.560132"));
    rates.put("JPY", new BigDecimal("132.360679"));
    rates.put("USD", new BigDecimal("1.23396"));

    exchangeRateUpdateService.refreshRates();

    assertEquals(rates, exchangeRateCacheService.getExchangeRates("EUR"));
  }

  @Test
  public void getLatestRates_shouldExistInDatabase() {
    exchangeRateUpdateService.refreshRates();

    String sql = "SELECT COUNT(*) FROM exchange_rates WHERE base_currency = ?";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, "EUR");

    assertNotNull(count, "Query returned null, but it should return the number of records");
    assertEquals(
        4, (int) count, "Expected 4 exchange rates for EUR in the database, but found: " + count);
  }

  @Test
  public void logEntry_shouldExistInDatabase() {
    exchangeRateUpdateService.refreshRates();

    String sql = "SELECT COUNT(*) FROM api_logs";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class);

    assertNotNull(count, "Query returned null, but it should return the number of log entries");
    assertEquals(
        1, (int) count, "Expected at least one log entry in api_logs, but found: " + count);
  }
}
