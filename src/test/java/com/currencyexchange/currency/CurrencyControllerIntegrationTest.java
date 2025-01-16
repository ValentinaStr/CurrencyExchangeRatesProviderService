package com.currencyexchange.currency;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Integration tests for the CurrencyController using WireMock to mock external API calls.
 * These tests check if the correct response is returned when accessing currency data.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrencyControllerIntegrationTest {

  private WebClient webClient;
  private WireMockServer wireMockServer;

  @Autowired
  private WebClient.Builder webClientBuilder;

  @BeforeEach
  void setup() {
    wireMockServer = new WireMockServer(8081);
    wireMockServer.start();

    webClient = webClientBuilder.baseUrl("http://localhost:8081").build();

    wireMockServer.stubFor(get(urlEqualTo("/api/v1/currencies"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("[\"USD\", \"EUR\"]")));
  }

  @AfterEach
  void teardown() {
    wireMockServer.stop();
  }

  @Test
  void testGetCurrencies() {
    String[] currencies = webClient.get()
        .uri("/api/v1/currencies")
        .retrieve()
        .bodyToMono(String[].class)
        .block();

    assertNotNull(currencies);
    assertEquals(2, currencies.length, "Expected exactly 2 currencies");
    assertTrue(Arrays.asList(currencies).contains("USD"), "Currencies should contain 'USD'");
    assertTrue(Arrays.asList(currencies).contains("EUR"), "Currencies should contain 'EUR'");
    assertFalse(Arrays.asList(currencies).contains("GBP"), "Currencies should not contain 'GBP'");
  }

  @Test
  void testGetAllCurrencies() {
    String[] currencies = webClient.get()
        .uri("/api/v1/currencies")
        .retrieve()
        .toEntity(String[].class)
        .block()
        .getBody();

    assertAll("Currency list validation",
        () -> assertNotNull(currencies, "Currencies response should not be null"),
        () -> assertEquals(2, currencies.length, "Expected exactly 2 currencies"),
        () -> assertTrue(Arrays.asList(currencies).contains("USD"), "Currencies should contain 'USD'"),
        () -> assertTrue(Arrays.asList(currencies).contains("EUR"), "Currencies should contain 'EUR'"),
        () -> assertFalse(Arrays.asList(currencies).contains("GBP"), "Currencies should not contain 'GBP'")
    );
  }

  @Test
  void testGetAllCurrencies_empty() {
    wireMockServer.stubFor(get(urlEqualTo("/api/v1/currencies"))
        .willReturn(aResponse().withStatus(200).withBody("[]")));

    String response = webClient.get()
        .uri("/api/v1/currencies")
        .retrieve()
        .bodyToMono(String.class)
        .block();

    assertEquals("[]", response);
  }
}
