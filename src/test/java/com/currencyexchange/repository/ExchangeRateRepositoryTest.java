package com.currencyexchange.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.currencyexchange.config.TestContainerConfig;
import com.currencyexchange.model.ExchangeRateEntity;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class ExchangeRateRepositoryTest extends TestContainerConfig {

  @Autowired
  private ExchangeRateRepository exchangeRateRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() {
    jdbcTemplate.update("DELETE FROM exchange_rates");
  }

  @Test
  void addExchangeRate_shouldReturnExchangeRateRateExists() {
    String sql =
        "INSERT INTO exchange_rates (base_currency, target_currency, rate) VALUES (?, ?, ?)";
    jdbcTemplate.update(sql, "EUR", "USD", new BigDecimal("23"));

    Optional<ExchangeRateEntity> result =
        exchangeRateRepository.findByBaseCurrencyAndTargetCurrency("EUR", "USD");

    assertThat(result)
        .isPresent()
        .hasValueSatisfying(
            rate -> {
              assertThat(rate.getBaseCurrency()).isEqualTo("EUR");
              assertThat(rate.getTargetCurrency()).isEqualTo("USD");
              assertThat(rate.getRate()).isEqualByComparingTo(new BigDecimal("23.000000"));
            });
  }

  @Test
  void addExchangeRate_shouldReturnEmptyRateNotExist() {

    Optional<ExchangeRateEntity> result =
        exchangeRateRepository.findByBaseCurrencyAndTargetCurrency("EUW", "GBP");

    assertThat(result).isNotPresent();
  }

  @Test
  void addExchangeRate_shouldReturnEmptyTargetCurrencyNotExist() {

    String sql =
        "INSERT INTO exchange_rates (base_currency, target_currency, rate) VALUES (?, ?, ?)";
    jdbcTemplate.update(sql, "EUR", "USD", new BigDecimal("23.000000"));

    Optional<ExchangeRateEntity> result =
        exchangeRateRepository.findByBaseCurrencyAndTargetCurrency("EUR", "GBP");

    assertThat(result).isNotPresent();
  }
}
