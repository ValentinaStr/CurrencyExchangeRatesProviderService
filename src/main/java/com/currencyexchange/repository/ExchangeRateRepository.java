package com.currencyexchange.repository;

import com.currencyexchange.entity.ExchangeRateEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRateEntity, Long> {

  /**
   * Finds an exchange rate by base and target currency.
   *
   * @param baseCurrency the base currency
   * @param targetCurrency the target currency
   * @return an {@link Optional} with the {@link ExchangeRateEntity} if found, empty otherwise
   */
  Optional<ExchangeRateEntity> findByBaseCurrencyAndTargetCurrency(
      String baseCurrency, String targetCurrency);

  /**
   * Finds all exchange rates for a given base currency.
   *
   * @param baseCurrency the base currency (e.g., "EUR")
   * @return a list of {@link ExchangeRateEntity} for the given base currency
   */
  List<ExchangeRateEntity> findByBaseCurrency(String baseCurrency);
}
