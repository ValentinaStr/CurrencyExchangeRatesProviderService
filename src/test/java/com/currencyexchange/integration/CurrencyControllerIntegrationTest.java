package com.currencyexchange.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.currencyexchange.config.TestContainerConfig;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerIntegrationTest extends TestContainerConfig {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() {
    jdbcTemplate.execute("DELETE FROM currencies");
  }

  @Transactional
  @Test
  void testGetAllCurrencies() throws Exception {
    jdbcTemplate.update("INSERT INTO currencies (id, currency) VALUES (?, ?)",
        UUID.randomUUID(), "USD");
    jdbcTemplate.update("INSERT INTO currencies (id, currency) VALUES (?, ?)",
        UUID.randomUUID(), "EUR");

    mockMvc.perform(get("/api/v1/currencies/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(content().json("[\"USD\",\"EUR\"]"));
  }

  @Transactional
  @Test
  void testAddCurrency() throws Exception {
    String newCurrency = "GBP";

    mockMvc.perform(post("/api/v1/currencies/")
            .param("currency", newCurrency))
        .andExpect(status().isCreated())
        .andExpect(content().string("Currency added: " + newCurrency));
  }

  @Transactional
  @Test
  void testAddInvalidCurrency() throws Exception {
    String invalidCurrency = "INV";

    mockMvc.perform(post("/api/v1/currencies/")
            .param("currency", invalidCurrency))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid currency: " + invalidCurrency));
  }

  @Transactional
  @Test
  void testAddExistingCurrency() throws Exception {
    String existingCurrency = "USD";
    jdbcTemplate.update("INSERT INTO currencies (id, currency) VALUES (?, ?)",
        UUID.randomUUID(), existingCurrency);

    mockMvc.perform(post("/api/v1/currencies/")
            .param("currency", existingCurrency))
        .andExpect(status().isOk())
        .andExpect(content().string("Currency already exists: " + existingCurrency));
  }
}
