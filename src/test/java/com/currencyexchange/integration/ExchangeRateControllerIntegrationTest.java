package com.currencyexchange.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.currencyexchange.business.ExchangeRateCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ExchangeRateControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ExchangeRateCacheService exchangeRateCacheService;

  @BeforeEach
  void setUp() {
    exchangeRateCacheService.clearCache();
  }

  @Test
  void testGetExchangeRateWhenRateCachedIsFound() throws Exception {
    exchangeRateCacheService.addExchangeRate("USD", 1.25);
    mockMvc.perform(get("/exchange-rates")
            .param("currency", "USD"))
        .andExpect(status().isOk())
        .andExpect(content().string("Exchange rate for USD: 1.25"));
  }
}
