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
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
class CurrencyUpdateIntegrationTest extends TestContainerConfig {

  @Value("${fixer.api.key}")
  private String fixerApiKey;

  @Value("${exchangeratesapi.api.key}")
  private String exchangeratesapiApiKey;

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
    registry.add("${exchangeratesapi.api.url}", wireMockExtension::baseUrl);
  }

  @BeforeEach
  void setUp() {

    jdbcTemplate.update("DELETE FROM api_logs");
    jdbcTemplate.update("DELETE FROM exchange_rates");
    jdbcTemplate.update("DELETE FROM currencies");
    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "EUR");

    String fiixerMockResponse =
        "{"
            + "\"success\": true,"
            + "\"timestamp\": 1519296206,"
            + "\"base\": \"EUR\","
            + "\"date\": \"2025-02-04\","
            + "\"rates\": {"
            + "\"AUD\": 1.566015,"
            + "\"CAD\": 2.560132,"
            + "\"JPY\": 132.360679,"
            + "\"USD\": 2.23396"
            + "}"
            + "}";

    String exchangeratesapiMockResponse =
        "{"
            + "\"success\": true,"
            + "\"timestamp\": 1519296206,"
            + "\"base\": \"EUR\","
            + "\"date\": \"2025-02-04\","
            + "\"rates\": {"
            + "\"AUD\": 2.566015,"
            + "\"CAD\": 1.560132,"
            + "\"JPY\": 232.360679,"
            + "\"USD\": 1.23396"
            + "}"
            + "}";

    String urlFixer = String.format("/latest?access_key=%s&base=%s", fixerApiKey, "EUR");
    wireMockExtension.stubFor(
        get(urlFixer)
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody(fiixerMockResponse)
                    .withHeader("Content-Type", "application/json")));

    String urlExchangeratesapi = String.format("/latest?access_key=%s", exchangeratesapiApiKey);
    wireMockExtension.stubFor(
        get(urlExchangeratesapi)
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody(exchangeratesapiMockResponse)
                    .withHeader("Content-Type", "application/json")));
  }

  @Test
  public void getLatestRates_shouldExistInCache() {
    Map<String, BigDecimal> rates = new HashMap<>();
    rates.put("AUD", new BigDecimal("2.566015"));
    rates.put("CAD", new BigDecimal("2.560132"));
    rates.put("JPY", new BigDecimal("232.360679"));
    rates.put("USD", new BigDecimal("2.23396"));

    exchangeRateUpdateService.refreshRates();

    assertEquals(rates, exchangeRateCacheService.getExchangeRates("EUR"));
  }

  @Test
  public void getLatestRates_shouldExistInDatabase() {
    Map<String, BigDecimal> rates = new HashMap<>();
    rates.put("AUD", new BigDecimal("2.566015"));
    rates.put("CAD", new BigDecimal("2.560132"));
    rates.put("JPY", new BigDecimal("232.360679"));
    rates.put("USD", new BigDecimal("2.233960"));
    String sql = "SELECT target_currency, rate FROM exchange_rates WHERE base_currency = ?";

    exchangeRateUpdateService.refreshRates();

    Map<String, BigDecimal> databaseRates =
        jdbcTemplate.queryForList(sql, "EUR").stream()
            .collect(
                Collectors.toMap(
                    row -> (String) row.get("target_currency"),
                    row -> ((BigDecimal) row.get("rate")).setScale(6, RoundingMode.HALF_UP)));
    assertEquals(rates, databaseRates);
  }

  @Test
  public void logEntry_shouldExistInDatabase() {
    exchangeRateUpdateService.refreshRates();

    String sql = "SELECT COUNT(*) FROM api_logs";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class);

    assertNotNull(count, "Query returned null, but it should return the number of log entries");
    assertEquals(
        2, (int) count, "Expected at least one log entry in api_logs, but found: " + count);
  }

  @Test
  public void cacheAndDatabaseRates_shouldMatch() {
    exchangeRateUpdateService.refreshRates();

    Map<String, BigDecimal> cachedRates = exchangeRateCacheService.getExchangeRates("EUR");
    cachedRates.replaceAll((k, v) -> v.setScale(6, RoundingMode.HALF_UP));

    String sql = "SELECT target_currency, rate FROM exchange_rates WHERE base_currency = ?";
    Map<String, BigDecimal> databaseRates =
        jdbcTemplate.queryForList(sql, "EUR").stream()
            .collect(
                Collectors.toMap(
                    row -> (String) row.get("target_currency"),
                    row -> ((BigDecimal) row.get("rate")).setScale(6, RoundingMode.HALF_UP)));

    assertEquals(databaseRates, cachedRates, "Rates in cache and database should match");
  }
}
