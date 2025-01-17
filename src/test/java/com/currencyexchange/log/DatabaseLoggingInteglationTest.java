package com.currencyexchange.log;

import com.currencyexchange.currency.CurrencyRepository;
import com.currencyexchange.currency.model.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import com.currencyexchange.log.model.LogEntry;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EnableAspectJAutoProxy(proxyTargetClass = false)
public class DatabaseLoggingInteglationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CurrencyRepository currencyRepository;

  @Autowired
  private LogEntryRepository logEntryRepository;

  @Container
  private static final PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:15-alpine");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.jpa.generate-ddl", () -> true);
  }

  @Transactional
  @Test
  void testCurrencyEndpointLogsRequest() throws Exception {
    Currency newCurrency = new Currency(null, "USD");
    currencyRepository.save(newCurrency);

    mockMvc.perform(get("/api/v1/currencies"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    List<LogEntry> logs = logEntryRepository.findAll();
    assertFalse(logs.isEmpty(), "The log entry must not be empty.");

    LogEntry logEntry = logs.getFirst();
    assertNotNull(logEntry.getTimestamp(), "Timestamp should not be null");
    assertEquals("/api/v1/currencies", logEntry.getUrl(), "URL does not match");
    assertEquals("USD", logEntry.getResponse(), "Response does not match");
  }

  @Transactional
  @Test
  void testCurrencyEndpointLogsRequestWhenDatabaseHasThreeEntities() throws Exception {
    currencyRepository.save(new Currency(null, "USD"));
    currencyRepository.save(new Currency(null, "EUR"));
    currencyRepository.save(new Currency(null, "GBP"));

    mockMvc.perform(get("/api/v1/currencies"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    List<LogEntry> logs = logEntryRepository.findAll();
    assertFalse(logs.isEmpty(), "The log entry must not be empty.");

    LogEntry logEntry = logs.getFirst();
    assertNotNull(logEntry.getTimestamp(), "Timestamp should not be null");
    assertEquals("/api/v1/currencies", logEntry.getUrl(), "URL does not match");
    assertEquals("USD, EUR, GBP", logEntry.getResponse(), "Response does not match");
  }

  @Transactional
  @Test
  void testCurrencyEndpointLogsRequestWithTwoRequests() throws Exception {
    currencyRepository.save(new Currency(null, "USD"));
    currencyRepository.save(new Currency(null, "EUR"));
    currencyRepository.save(new Currency(null, "GBP"));

    mockMvc.perform(get("/api/v1/currencies"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    mockMvc.perform(get("/api/v1/currencies"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    List<LogEntry> logsAfterSecondRequest = logEntryRepository.findAll();
    int logCountAfterSecondRequest = logsAfterSecondRequest.size();

    assertEquals(2, logCountAfterSecondRequest, "There should be two log entries after the second request");

    LogEntry secondLogEntry = logsAfterSecondRequest.get(1);
    assertNotNull(secondLogEntry.getTimestamp(), "Timestamp should not be null");
    assertEquals("/api/v1/currencies", secondLogEntry.getUrl(), "URL does not match");
    assertEquals("USD, EUR, GBP", secondLogEntry.getResponse(), "Response does not match");
  }
}
