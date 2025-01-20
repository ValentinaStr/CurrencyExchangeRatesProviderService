package com.currencyexchange.currency;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.currencyexchange.currency.model.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@EnableAspectJAutoProxy()
public class CurrencyControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CurrencyRepository currencyRepository;

  @Container
  private static final PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:15-alpine"));

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.jpa.generate-ddl", () -> true);
  }

  @Test
  void testGetAllCurrencies() throws Exception {
    currencyRepository.save(new Currency(null, "USD"));
    currencyRepository.save(new Currency(null, "EUR"));

    mockMvc.perform(get("/api/v1/currencies"))
        .andExpect(status().isOk()) // Assert: Expect 200 OK status
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0]").value("USD"))
        .andExpect(jsonPath("$[1]").value("EUR"));
  }

  @Test
  void testGetAllCurrencies_empty() throws Exception {
    mockMvc.perform(get("/api/v1/currencies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
  }
}
