package com.currencyexchange.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.currencyexchange.business.ApiLogService;
import com.currencyexchange.dto.ExchangeratesapiClientDto;
import com.currencyexchange.exception.ExchangeRateClientUnavailableException;
import com.currencyexchange.mapper.ResponseModelMapper;
import com.currencyexchange.model.RatesModel;
import java.math.BigDecimal;
import java.util.Map;
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
class ExchangeratesapiClientTest {

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private ApiLogService apiLogService;

  @Mock
  private ResponseModelMapper responseModelMapper;

  @InjectMocks
  private ExchangeratesapiClient exchangeratesapiClient;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(exchangeratesapiClient, "apiKey", "test-api-key");
    ReflectionTestUtils.setField(exchangeratesapiClient, "apiUrl", "https://api.exchangeratesapi.io");
  }

  @Test
  void getExchangeRate_shouldReturnRatesWhenApiCallIsSuccessful() {
    String url = "https://api.exchangeratesapi.io/latest?access_key=test-api-key";
    ExchangeratesapiClientDto mockResponse = new ExchangeratesapiClientDto(
        true, 1519296206L, "EUR", Map.of("USD", new BigDecimal("1.1")));
    RatesModel ratesModel = new RatesModel(1519296206L, "EUR",
        Map.of("USD", new BigDecimal("1.1")));
    when(restTemplate.getForObject(url, ExchangeratesapiClientDto.class)).thenReturn(mockResponse);
    when(responseModelMapper.exchangeratesDtoToRatesModel(mockResponse)).thenReturn(ratesModel);

    RatesModel result = exchangeratesapiClient.getExchangeRate("EUR");

    assertEquals("EUR", result.base());
    assertEquals(new BigDecimal("1.1"), result.rates().get("USD"));
    verify(apiLogService).logRequest("https://api.exchangeratesapi.io", ratesModel);
  }

  @Test
  void getExchangeRate_shouldReturnNullWhenResponseIsUnsuccessful() {
    String url = "https://api.exchangeratesapi.io/latest?access_key=test-api-key";
    ExchangeratesapiClientDto mockResponse = new ExchangeratesapiClientDto(
        false, 1519296206L, "EUR", Map.of());
    when(restTemplate.getForObject(url, ExchangeratesapiClientDto.class)).thenReturn(mockResponse);

    RatesModel result = exchangeratesapiClient.getExchangeRate("EUR");

    assertNull(result);
    verifyNoInteractions(apiLogService);
  }

  @Test
  void getExchangeRate_shouldThrowWhenApiCallFails() {
    String url = "https://api.exchangeratesapi.io/latest?access_key=test-api-key";
    when(restTemplate.getForObject(url, ExchangeratesapiClientDto.class))
        .thenThrow(new RestClientException("API error"));

    assertThrows(ExchangeRateClientUnavailableException.class,
        () -> exchangeratesapiClient.getExchangeRate("EUR"));
  }
}
