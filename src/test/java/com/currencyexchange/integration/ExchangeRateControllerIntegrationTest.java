package com.currencyexchange.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.currencyexchange.cache.ExchangeRateCacheService;
import com.currencyexchange.config.TestContainerConfig;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ExchangeRateControllerIntegrationTest extends TestContainerConfig {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ExchangeRateCacheService exchangeRateCacheService;

  @BeforeEach
  void setUp() {
    Map<String, Map<String, BigDecimal>> rates = new HashMap<>();
    Map<String, BigDecimal> usdRates = new HashMap<>();
    usdRates.put("EUR", new BigDecimal("1.18"));
    usdRates.put("USD", new BigDecimal("1.28"));
    rates.put("GBP", usdRates);
    exchangeRateCacheService.updateAll(rates);
  }

  @Test
  @WithMockUser(username = "user", roles = "USER")
  void getExchangeRate_shouldReturnRatesForUser() throws Exception {
    mockMvc
        .perform(get("/api/v1/exchange-rates").param("currency", "GBP"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currency").value("GBP"))
        .andExpect(jsonPath("$.rates").isMap())
        .andExpect(jsonPath("$.rates.EUR").value(1.18))
        .andExpect(jsonPath("$.rates.USD").value(1.28));
  }

  @Test
  @WithMockUser(username = "admin", roles = "ADMIN")
  void getExchangeRate_shouldReturnRatesForAdmin() throws Exception {
    mockMvc
        .perform(get("/api/v1/exchange-rates").param("currency", "GBP"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currency").value("GBP"))
        .andExpect(jsonPath("$.rates").isMap())
        .andExpect(jsonPath("$.rates.EUR").value(1.18))
        .andExpect(jsonPath("$.rates.USD").value(1.28));
  }

  @Test
  void getExchangeRate_shouldReturnUnauthorizedForUnauthorizedUser() throws Exception {
    mockMvc
        .perform(get("/exchange-rates").param("currency", "GBP"))
        .andExpect(status().isUnauthorized());
  }
}
