package com.currencyexchange.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.business.ApiLogService;
import com.currencyexchange.dto.ExchangeRateResponseDto;
import com.currencyexchange.exception.ExchangeRateClientUnavailableException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class ExchangeRateClientTests {

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private ApiLogService apiLogService;

  @InjectMocks
  private ExchangeratesapiClient exchangeratesapiClient;

  @InjectMocks
  private FixerClient fixerClient;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(exchangeratesapiClient, "apiKey", "test-api-key");
    ReflectionTestUtils.setField(
        exchangeratesapiClient, "apiUrl", "https://api.exchangeratesapi.io");

    ReflectionTestUtils.setField(fixerClient, "apiKey", "test-api-key");
    ReflectionTestUtils.setField(fixerClient, "apiUrl", "http://api.fixer.io");
  }

  @ParameterizedTest
  @CsvSource({
    "exchangeratesapi, https://api.exchangeratesapi.io/latest?access_key=test-api-key",
    "fixer, http://api.fixer.io/latest?access_key=test-api-key&base=EUR"
  })
  void getExchangeRate_shouldReturnResponseApiCallIsSuccessful(
      String clientType, String expectedUrl) {
    String currency = "EUR";
    ExchangeRateResponseDto mockResponse =
        new ExchangeRateResponseDto(true, 1519296206L, "EUR", Map.of("USD", new BigDecimal("1.1")));

    when(restTemplate.getForObject(expectedUrl, ExchangeRateResponseDto.class))
        .thenReturn(mockResponse);

    ExchangeRateClient client =
        clientType.equals("exchangeratesapi") ? exchangeratesapiClient : fixerClient;
    ExchangeRateResponseDto response = client.getExchangeRate(Set.of(currency));
    assertNotNull(response);
    assertEquals("EUR", response.base());
    assertEquals(1, response.rates().size());
    assertEquals(new BigDecimal("1.1"), response.rates().get("USD"));
    verify(apiLogService).logRequest(expectedUrl, mockResponse);
  }

  @ParameterizedTest
  @CsvSource({
    "exchangeratesapi, https://api.exchangeratesapi.io/latest?access_key=test-api-key",
    "fixer, http://api.fixer.io/latest?access_key=test-api-key&base=EUR"
  })
  void getExchangeRate_shouldThrowExceptionApiCallFails(String clientType, String expectedUrl) {
    String currency = "EUR";
    when(restTemplate.getForObject(expectedUrl, ExchangeRateResponseDto.class))
        .thenThrow(new RestClientException("API error"));

    ExchangeRateClient client =
        clientType.equals("exchangeratesapi") ? exchangeratesapiClient : fixerClient;
    Exception exception =
        assertThrows(
            ExchangeRateClientUnavailableException.class,
            () -> client.getExchangeRate(Set.of(currency)));

    assertTrue(
        exception.getMessage().contains("Failed to fetch exchange rates from: " + expectedUrl));
  }
}
