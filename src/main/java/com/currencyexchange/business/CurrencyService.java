package com.currencyexchange.business;

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
   * Adds a new currency to the system by saving it to the repository.
   *
   * @param currency The {@link Currency} object to be added. It must contain a valid 3-letter
   *                 uppercase currency code (e.g., 'USD').
   */
  public void addCurrency(Currency currency) {
    repository.save(currency);
  }
}
