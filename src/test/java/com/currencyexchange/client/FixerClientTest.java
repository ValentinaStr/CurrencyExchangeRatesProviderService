package com.currencyexchange.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.business.ApiLogService;
import com.currencyexchange.dto.FixerDto;
import com.currencyexchange.exception.ExchangeRateClientUnavailableException;
import com.currencyexchange.model.Rates;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class FixerClientTest {

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private ApiLogService apiLogService;

  @InjectMocks
  private FixerClient fixerClient;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(fixerClient, "apiKey", "test-api-key");
    ReflectionTestUtils.setField(fixerClient, "apiUrl", "https://api.fixer.io");
  }

  @Test
  void getExchangeRate_shouldReturnResponse_whenApiCallIsSuccessful() {
    String currency = "EUR";
    String url = "https://api.fixer.io/latest?access_key=test-api-key&base=EUR";

    FixerDto mockResponse =
        new FixerDto(true, 1519296206L, "EUR", Map.of("USD", new BigDecimal("1.1")));

    when(restTemplate.getForObject(url, FixerDto.class)).thenReturn(mockResponse);

    Rates response = fixerClient.getExchangeRate(Set.of(currency));

    assertNotNull(response);
    assertEquals("EUR", response.base());
    assertEquals(1, response.rates().size());
    assertEquals(new BigDecimal("1.1"), response.rates().get("USD"));

    verify(apiLogService).logRequest("https://api.fixer.io", response);
  }

  @Test
  void getExchangeRate_shouldThrowException_whenApiCallFails() {
    String currency = "EUR";
    String url = "https://api.fixer.io/latest?access_key=test-api-key&base=EUR";

    when(restTemplate.getForObject(url, FixerDto.class))
        .thenThrow(new RestClientException("API error"));

    Exception exception =
        assertThrows(
            ExchangeRateClientUnavailableException.class,
            () -> {
              fixerClient.getExchangeRate(Set.of(currency));
            });

    assertTrue(exception.getMessage().contains("Failed to fetch exchange rates from: " + url));
  }
}
