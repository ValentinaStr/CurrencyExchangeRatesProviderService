package com.currencyexchange.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Transactional
  @Test
  void testGetAllCurrencies() throws Exception {
    jdbcTemplate.execute("DELETE FROM currencies");
    jdbcTemplate.update("INSERT INTO currencies (id, currency) VALUES (?, ?)",
        UUID.randomUUID(), "USD");
    jdbcTemplate.update("INSERT INTO currencies (id, currency) VALUES (?, ?)",
        UUID.randomUUID(), "EUR");

    mockMvc.perform(get("/api/v1/currencies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0]").value("EUR"))
        .andExpect(jsonPath("$[1]").value("USD"));
  }
}
