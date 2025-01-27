package com.currencyexchange.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.currencyexchange.config.TestContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerIntegrationTest extends TestContainerConfig {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  /**
   * Clears the currencies table before each test.
   */
  @BeforeEach
  public void setUp() {
    jdbcTemplate.update("DELETE FROM currencies");
  }

  @Test
  void testGetAllCurrencies() throws Exception {
    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "USD");
    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "EUR");

    mockMvc.perform(get("/api/v1/currencies/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(content().json("[\"USD\",\"EUR\"]"));
  }

  @Test
  void testAddCurrency_validCurrency() throws Exception {
    mockMvc.perform(post("/api/v1/currencies/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"currency\":\"GBP\"}"))
        .andExpect(status().isOk())
        .andExpect(content().string("Currency processed: GBP"));
  }

  @Test
  void testAddCurrency_existingCurrency() throws Exception {
    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "GBP");

    mockMvc.perform(post("/api/v1/currencies/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"currency\":\"GBP\"}"))
        .andExpect(status().isOk())
        .andExpect(content().string("Currency processed: GBP"));
  }
}
