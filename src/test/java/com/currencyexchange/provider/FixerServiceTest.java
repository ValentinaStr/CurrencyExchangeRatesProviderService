package com.currencyexchange.provider;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.currencyexchange.business.ApiLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@SpringBootTest
public class FixerServiceTest {

  @Mock private RestTemplate restTemplate;

  @Mock private ApiLogService apiLogService;

  @InjectMocks private FixerService fixerService;

  @Test
  void getLatestRates_shouldReturnRates() {
    Set<String> baseCurrencies = new HashSet<>(Arrays.asList("USD", "EUR"));

    Map<String, Double> rates =
        Map.of(
            "USD", 1.0,
            "EUR", 0.85);
    FixerResponse fixerResponse =
        FixerResponse.builder().base("USD").date("2025-02-04").rates(rates).build();

    when(restTemplate.getForObject(anyString(), eq(FixerResponse.class))).thenReturn(fixerResponse);

    Map<String, Map<String, BigDecimal>> result = fixerService.getLatestRates(baseCurrencies);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(new BigDecimal("1.0"), result.get("USD").get("USD"));
    assertEquals(new BigDecimal("0.85"), result.get("USD").get("EUR"));
  }

  /* @Test
  void getLatestRates_shouldHandleEmptyRates() {
      // Arrange
      Set<String> baseCurrencies = new HashSet<>(Arrays.asList("USD"));
      var fixerResponse = new FixerResponse();
      fixerResponse.setBase("USD");
      fixerResponse.setRates(Collections.emptyMap());


      when(restTemplate.getForObject(anyString(), eq(FixerResponse.class)))
              .thenReturn(fixerResponse);

      // Act
      Map<String, Map<String, BigDecimal>> result = fixerService.getLatestRates(baseCurrencies);

      // Assert
      assertNotNull(result);
      assertTrue(result.isEmpty());  // No rates available, should return an empty map

      // Verify that saveApiLog was called
      verify(apiLogService, times(1)).saveApiLog(anyString(), any(FixerResponse.class));
  }*/

  /* @Test
  void getLatestRates_shouldHandleNullResponse() {
      // Arrange
      Set<String> baseCurrencies = new HashSet<>(Arrays.asList("USD"));

      // Mock RestTemplate to return null (simulating an API failure or empty response)
      when(restTemplate.getForObject(anyString(), eq(FixerResponse.class)))
              .thenReturn(null);

      // Act
      Map<String, Map<String, BigDecimal>> result = fixerService.getLatestRates(baseCurrencies);

      // Assert
      assertNotNull(result);
      assertTrue(result.isEmpty());  // Since response is null, we expect an empty map

      // Verify that saveApiLog was called
      verify(apiLogService, times(1)).saveApiLog(anyString(), any(FixerResponse.class));
  }*/
}
