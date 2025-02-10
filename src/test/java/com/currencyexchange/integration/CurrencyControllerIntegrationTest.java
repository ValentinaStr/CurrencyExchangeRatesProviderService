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
class CurrencyControllerIntegrationTest extends TestContainerConfig {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  /** Clears the currencies table before each test. */
  @BeforeEach
  void setUp() {
    jdbcTemplate.update("DELETE FROM currencies");
  }

  @Test
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
        .perform(get("/api/v1/currencies/").header("Authorization", "Basic dXNlcjp1c2VyMTIz"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currencies").isArray())
        .andExpect(content().json(expectedCurrenciesJson));
  }

  @Test
  void addCurrency_shouldReturnNotFoundWhenCurrencyIsValidForUser() throws Exception {
    String validCurrencyJson =
        """
        {
          "currency": "GBP"
        }
        """;

    mockMvc
        .perform(
            post("/api/v1/currencies/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCurrencyJson)
                .header("Authorization", "Basic dXNlcjp1c2VyMTIz"))
        .andExpect(status().isNotFound());
  }

  @Test
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
            post("/api/v1/currencies/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(existingCurrencyJson)
                .header("Authorization", "Basic dXNlcjp1c2VyMTIz"))
        .andExpect(status().isNotFound());
  }

  @Test
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
        .perform(get("/api/v1/currencies/").header("Authorization", "Basic YWRtaW46YWRtaW4xMjM="))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currencies").isArray())
        .andExpect(content().json(expectedCurrenciesJson));
  }

  @Test
  void addCurrency_shouldReturnCreatedWhenCurrencyIsValidForAdmin() throws Exception {
    String validCurrencyJson =
        """
        {
          "currency": "GBP"
        }
        """;

    mockMvc
        .perform(
            post("/api/v1/currencies/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCurrencyJson)
                .header("Authorization", "Basic YWRtaW46YWRtaW4xMjM="))
        .andExpect(status().isCreated())
        .andExpect(content().json("{\"currency\":\"GBP\"}"));
  }

  @Test
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
            post("/api/v1/currencies/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(existingCurrencyJson)
                .header("Authorization", "Basic YWRtaW46YWRtaW4xMjM="))
        .andExpect(status().isCreated())
        .andExpect(content().json("{\"currency\":\"GBP\"}"));
  }

  @Test
  void getAllCurrencies_shouldReturnUnauthorizedForUnauthorizedUser() throws Exception {
    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "USD");
    jdbcTemplate.update("INSERT INTO currencies (currency) VALUES (?)", "EUR");
    String expectedCurrenciesJson =
        """
            ["USD", "EUR"]
            """;

    mockMvc
        .perform(get("/api/v1/currencies/").header("Authorization", ""))
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
            post("/api/v1/currencies/")
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
            post("/api/v1/currencies/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(existingCurrencyJson))
        .andExpect(status().isUnauthorized());
  }
}
