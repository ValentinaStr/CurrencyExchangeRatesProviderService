package com.currencyexchange.repository;

import com.currencyexchange.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
  /**
   * Checks if a currency exists in the repository by its currency code.
   *
   * @param currency The currency code to check for existence in the repository.
   * @return true if a currency with the specified code exists in the repository, false otherwise.
   */
  boolean existsByCurrency(String currency);
}
