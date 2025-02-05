package com.currencyexchange.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

import com.currencyexchange.business.ExchangeRateUpdateService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@WireMockTest
@TestPropertySource(properties = {
        "fixer.api.key=71eb9f9d589f4b2c311dbda4dfac5bc3",
        "fixer.api.url=http://localhost:8089/api"
})
public class CurrencyExchangeServiceTest {

  @Autowired
  private ExchangeRateUpdateService exchangeRateUpdateService;

  @RegisterExtension
  static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
          .options(wireMockConfig().dynamicPort())
          .build();

  @DynamicPropertySource
  public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
    registry.add("${fixer.api.url}", wireMockExtension::baseUrl);
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
                    + "    \"AUD\": 1000000,\n"
                    + "    \"CAD\": 1.560132,\n"
                    + "    \"CHF\": 1.154727,\n"
                    + "    \"CNY\": 7.827874,\n"
                    + "    \"GBP\": 0.882047,\n"
                    + "    \"JPY\": 132.360679,\n"
                    + "    \"USD\": 0.23396\n"
                    + "  }\n"
                    + "}";

    String url = "/api?access_key=71eb9f9d589f4b2c311dbda4dfac5bc3&base=USD";

    wireMockExtension.stubFor(
            WireMock.get("/api?access_key=71eb9f9d589f4b2c311dbda4dfac5bc3&base=USD")  // Используем точное совпадение
                    .willReturn(
                            aResponse()
                                    .withStatus(200)
                                    .withBody(mockResponse)
                                    .withHeader("Content-Type", "application/json"))
    );

    // Вызов метода для получения курсов валют
    var e = exchangeRateUpdateService.refreshRates();

    // Проверка, что запрос был выполнен с правильным URL
    WireMock.verify(
            WireMock.getRequestedFor(urlEqualTo(url))  // Используем точное совпадение
    );

    // Получение всех запросов, сделанных к WireMock, и проверка их содержимого
    List<LoggedRequest> requests = findAll(WireMock.getRequestedFor(urlEqualTo(url)));  // Используем точное совпадение
    assertFalse(requests.isEmpty(), "No requests received by WireMock");
    assertTrue(requests.get(0).getUrl().contains("access_key"), "Request URL should contain access_key");
    assertTrue(requests.get(0).getUrl().contains("base=EUR"), "Request URL should contain base=EUR");
  }
}
