package com.currencyexchange.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

  @Test
  void getExchangeRate_shouldReturnRates() throws Exception {
    mockMvc.perform(get("/exchange-rates/")
            .param("currency", "GBP"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.EUR").value(1.18))
        .andExpect(jsonPath("$.USD").value(1.28))
        .andExpect(jsonPath("$.GBP").value(1.0));
  }
}
