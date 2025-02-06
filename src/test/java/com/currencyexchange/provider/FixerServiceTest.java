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
    Set<String> baseCurrencies = new HashSet<>(Arrays.asList("USD"));

    Map<String, Double> rates =
        Map.of(
            "EYR", 1.0,
            "RUB", 0.85);
    FixerResponse fixerResponse =
        FixerResponse.builder().base("USD").date("2025-02-04").rates(rates).build();

    when(restTemplate.getForObject(anyString(), eq(FixerResponse.class))).thenReturn(fixerResponse);

    Map<String, Map<String, BigDecimal>> result = fixerService.getLatestRates(baseCurrencies);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(new BigDecimal("1.0"), result.get("USD").get("EYR"));
    assertEquals(new BigDecimal("0.85"), result.get("USD").get("RUB"));
  }
}
