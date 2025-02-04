package com.currencyexchange.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

import com.currencyexchange.provider.ExchangeRateProvider;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
public class CurrencyExchangeServiceTest {

  @Autowired
  private ExchangeRateProvider currencyExchangeService;

  @RegisterExtension
  static WireMockExtension wireMockExtension =
          WireMockExtension.newInstance().options(wireMockConfig().port(8100)).build();

  @DynamicPropertySource
  public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
    registry.add("${currency.api.url}", () -> "http://localhost:8100");
  }

  @Test
  public void getLatestRates_ShouldReturnRates() {

    // Мок-ответ от фальшивого API с курсами валют
    String mockResponse =
            "{\n"
                    + "  \"success\": true,\n"
                    + "  \"timestamp\": 1519296206,\n"
                    + "  \"base\": \"EUR\",\n"
                    + "  \"date\": \"2021-02-04\",\n"
                    + "  \"rates\": {\n"
                    + "    \"AUD\": 1.566015,\n"
                    + "    \"CAD\": 1.560132,\n"
                    + "    \"CHF\": 1.154727,\n"
                    + "    \"CNY\": 7.827874,\n"
                    + "    \"GBP\": 0.882047,\n"
                    + "    \"JPY\": 132.360679,\n"
                    + "    \"USD\": 1.23396\n"   // Убедитесь, что это значение правильное
                    + "  }\n"
                    + "}";

    // Настройка WireMock для ответа на запрос
    wireMockExtension.stubFor(
            WireMock.get(urlMatching("/api/latest\\?access_key=[^&]+&base=EUR"))
                    .willReturn(
                            aResponse()
                                    .withStatus(200)
                                    .withBody(mockResponse)
                                    .withHeader("Content-Type", "application/json")));

    // Вызов метода для получения курсов валют
    Map<String, Map<String, BigDecimal>> rates = currencyExchangeService.getLatestRates();

    // Печать полученных курсов (для отладки)
    System.out.println("Полученные курсы валют: " + rates);

    // Проверки, что данные корректно получены
    assertNotNull(rates);
    assertTrue(rates.containsKey("EUR"));

    Map<String, BigDecimal> eurRates = rates.get("EUR");
    assertEquals(new BigDecimal("1.033163"), eurRates.get("USD"));
    assertEquals(new BigDecimal("0.882047"), eurRates.get("GBP"));
    assertEquals(new BigDecimal("132.360679"), eurRates.get("JPY"));
    assertEquals(new BigDecimal("1.566015"), eurRates.get("AUD"));

    // Проверка, что WireMock был вызван с правильным URL
    verify(getRequestedFor(urlMatching("/api/latest\\?access_key=[^&]+&base=EUR")));

    // Получение всех запросов, сделанных к WireMock, и проверка их содержимого
    List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/api/latest\\?access_key=[^&]+&base=EUR")));
    assertFalse(requests.isEmpty());
    assertTrue(requests.get(0).getUrl().contains("access_key"));
  }
}
