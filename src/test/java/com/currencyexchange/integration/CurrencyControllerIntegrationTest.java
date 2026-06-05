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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerIntegrationTest extends TestContainerConfig {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() {
    jdbcTemplate.update("DELETE FROM currencies");
  }

  @Test
  @WithMockUser(username = "user", roles = "USER")
  void getAllCurrencies_shouldReturnListOfCurrenciesForUser() throws Exception {
    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "USD");
    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "EUR");

    String expectedCurrenciesJson =
        """
            {
              "currencies": ["USD", "EUR"]
            }
            """;

    mockMvc
        .perform(get("/api/v1/currencies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currencies").isArray())
        .andExpect(content().json(expectedCurrenciesJson));
  }

  @Test
  @WithMockUser(username = "user", roles = "USER")
  void addCurrency_shouldReturnNotFoundWhenCurrencyIsValidForUser() throws Exception {
    String validCurrencyJson =
        """
        {
          "currency": "GBP"
        }
        """;

    mockMvc
        .perform(
            post("/api/v1/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCurrencyJson))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "user", roles = "USER")
  void addCurrency_shouldReturnNotFoundCurrencyAlreadyExistsForUser() throws Exception {
    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "GBP");
    String existingCurrencyJson =
        """
        {
          "currency": "GBP"
        }
        """;

    mockMvc
        .perform(
            post("/api/v1/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(existingCurrencyJson))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "admin", roles = "ADMIN")
  void getAllCurrencies_shouldReturnListOfCurrenciesForAdmin() throws Exception {
    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "USD");
    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "EUR");

    String expectedCurrenciesJson =
        """
            {
              "currencies": ["USD", "EUR"]
            }
            """;

    mockMvc
        .perform(get("/api/v1/currencies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currencies").isArray())
        .andExpect(content().json(expectedCurrenciesJson));
  }

  @Test
  @WithMockUser(username = "admin", roles = "ADMIN")
  void addCurrency_shouldReturnCreatedWhenCurrencyIsValidForAdmin() throws Exception {
    String validCurrencyJson =
        """
        {
          "currency": "GBP"
        }
        """;

    mockMvc
        .perform(
            post("/api/v1/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCurrencyJson))
        .andExpect(status().isCreated())
        .andExpect(content().json("{\"message\": \"Currency processed: GBP\"}"));
  }

  @Test
  @WithMockUser(username = "admin", roles = "ADMIN")
  void addCurrency_shouldReturnCreatedWhenCurrencyAlreadyExistsForAdmin() throws Exception {
    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "GBP");
    String existingCurrencyJson =
        """
        {
          "currency": "GBP"
        }
        """;

    mockMvc
        .perform(
            post("/api/v1/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(existingCurrencyJson))
        .andExpect(status().isCreated())
        .andExpect(content().json("{\"message\": \"Currency processed: GBP\"}"));
  }

  @Test
  void getAllCurrencies_shouldReturnUnauthorizedForUnauthorizedUser() throws Exception {
    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "USD");
    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "EUR");

    mockMvc
        .perform(get("/api/v1/currencies"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void addCurrency_shouldReturnUnauthorizedForUnauthorizedUser() throws Exception {
    String validCurrencyJson =
        """
            {
              "currency": "GBP"
            }
            """;

    mockMvc
        .perform(
            post("/api/v1/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCurrencyJson))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void addCurrency_shouldReturnUnauthorizedWhenCurrencyAlreadyExistsForUnauthorizedUser()
      throws Exception {
    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "GBP");
    String existingCurrencyJson =
        """
            {
              "currency": "GBP"
            }
            """;

    mockMvc
        .perform(
            post("/api/v1/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(existingCurrencyJson))
        .andExpect(status().isUnauthorized());
  }
}
