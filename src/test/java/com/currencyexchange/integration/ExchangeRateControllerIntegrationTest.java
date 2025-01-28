package com.currencyexchange.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.currencyexchange.business.ExchangeRateCacheService;
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

  @Test
  void testGetExchangeRateWhenRateCachedIsFound() throws Exception {
    exchangeRateCacheService.getExchangeRate("GBP");

    mockMvc.perform(get("/exchange-rates/")
            .param("currency", "GBP"))
        .andExpect(status().isOk())
        .andExpect(content().string("Exchange rate for GBP: 1.2"));
  }
}
