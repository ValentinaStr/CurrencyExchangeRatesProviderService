package com.currencyexchange.business;

import static com.currencyexchange.enums.ValidCurrencyEnum.isValidCurrency;

import com.currencyexchange.exception.UnsupportedCurrencyException;
import com.currencyexchange.model.Currency;
import com.currencyexchange.repository.CurrencyRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CurrencyService {
  private final CurrencyRepository repository;

  /**
   * Retrieves a list of all currency codes available in the repository.
   *
   * @return A set of currency codes (e.g., "USD", "EUR") as a {@link Set} of {@link String}.
   */
  public Set<String> getAllCurrencies() {
    log.info("Fetching all currencies from the repository.");
    List<Currency> currencies = repository.findAll();
    log.info("Found {} currencies: {}", currencies.size(),
        currencies.stream().map(Currency::getCurrency).collect(Collectors.toList()));
    return currencies.stream()
        .map(Currency::getCurrency)
        .collect(Collectors.toSet());
  }

  /**
   * Adds a new currency to the repository if it is valid and does not already exist.
   *
   * @param currency The currency code to be added.
   * @return {@code true} if the currency was successfully added, {@code false} if the currency already exists.
   * @throws IllegalArgumentException if the currency is invalid (i.e., does not meet the required format).
   */
  public boolean addCurrency(String currency) {
    log.info("Attempting to add currency: {}", currency);

    if (!isValidCurrency(currency)) {
      throw new UnsupportedCurrencyException("Invalid currency: " + currency);
    } else if (repository.existsByCurrency(currency)) {
      return false;
    }
    Currency newCurrency = new Currency(null, currency);
    repository.save(newCurrency);
    log.info("Currency successfully added: {}", currency);
    return true;
  }
}
