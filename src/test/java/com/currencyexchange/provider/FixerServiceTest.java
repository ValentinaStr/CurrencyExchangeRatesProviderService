package com.currencyexchange.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import com.currencyexchange.business.ApiLogService;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class FixerServiceTest {

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private ApiLogService apiLogService;

  @InjectMocks
  private FixerService fixerService;

  @Test
  void getLatestRates_shouldReturnRates() {
    Set<String> baseCurrencies = new HashSet<>(List.of("USD"));
    Map<String, BigDecimal> rates = Map.of(
            "RUB", new BigDecimal("1.0"),
            "EUR", new BigDecimal("0.85")
    );
    FixerResponse fixerResponse =
        FixerResponse.builder().base("USD").date("2025-02-04").rates(rates).build();

    when(restTemplate.getForObject(anyString(), eq(FixerResponse.class))).thenReturn(fixerResponse);

    Map<String, Map<String, BigDecimal>> result = fixerService.getLatestRates(baseCurrencies);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(new BigDecimal("0.85"), result.get("USD").get("EUR"));
    assertEquals(new BigDecimal("1.0"), result.get("USD").get("RUB"));
  }
}
