package com.currencyexchange.integration;
import com.currencyexchange.provider.FixerService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {
        "fixer.api.key=71eb9f9d589f4b2c311dbda4dfac5bc3",
        "fixer.api.url=http://localhost:8081/api"
})
@WireMockTest(httpPort = 8081)
public class FixerServiceTest {

    @Autowired
    private FixerService fixerService;

  @BeforeEach
  public void setup() {

      WireMock.stubFor(get(urlEqualTo("/api?access_key=71eb9f9d589f4b2c311dbda4dfac5bc3&base=USD"))
              .willReturn(aResponse()
                      .withStatus(200)
                      .withHeader("Content-Type", "application/json")
                      .withBody("{"
                              + "\"success\": true,"
                              + "\"timestamp\": 1519296206,"
                              + "\"base\": \"USD\","
                              + "\"date\": \"2025-02-04\","
                              + "\"rates\": {"
                              + "\"AUD\": 1.566015,"
                              + "\"CAD\": 1.560132,"
                              + "\"CHF\": 1.154727,"
                              + "\"CNY\": 7.827874,"
                              + "\"GBP\": 0.882047,"
                              + "\"JPY\": 132.360679,"
                              + "\"USD\": 1.23396"
                              + "}"
                              + "}")));
        }

    @Test
    public void testGetLatestRates() {
        Set<String> currencies = Set.of("USD");

        Map<String, Map<String, BigDecimal>> rates = fixerService.getLatestRates(currencies);

        assertEquals(1, rates.size());

        assertEquals(new BigDecimal("1.23396"), rates.get("USD").get("USD"));
        assertEquals(new BigDecimal("1.566015"), rates.get("USD").get("AUD"));
        assertEquals(new BigDecimal("1.560132"), rates.get("USD").get("CAD"));
        assertEquals(new BigDecimal("1.154727"), rates.get("USD").get("CHF"));
        assertEquals(new BigDecimal("7.827874"), rates.get("USD").get("CNY"));
        assertEquals(new BigDecimal("0.882047"), rates.get("USD").get("GBP"));
        assertEquals(new BigDecimal("132.360679"), rates.get("USD").get("JPY"));
    }
}
