package com.currencyexchange.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.business.ApiLogService;
import com.currencyexchange.dto.ExchangeRateResponseDto;
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
    ReflectionTestUtils.setField(fixerClient, "apiUrl", "http://api.fixer.io");
  }

  @Test
  void getExchangeRate_shouldReturnResponseApiCallIsSuccessful() {
    String currency = "EUR";
    String url = "http://api.fixer.io/latest?access_key=test-api-key&base=EUR";
    ExchangeRateResponseDto mockResponse =
        new ExchangeRateResponseDto(true, 1519296206L, "EUR", Map.of("USD", new BigDecimal("1.1")));

    when(restTemplate.getForObject(url, ExchangeRateResponseDto.class)).thenReturn(mockResponse);

    ExchangeRateResponseDto response = fixerClient.getExchangeRate(Set.of(currency));

    assertNotNull(response);
    assertEquals("EUR", response.base());
    assertEquals(1, response.rates().size());
    assertEquals(new BigDecimal("1.1"), response.rates().get("USD"));
    verify(apiLogService).logRequest(url, mockResponse);
  }
}
