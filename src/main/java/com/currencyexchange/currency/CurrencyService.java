package com.currencyexchange.currency;

import com.currencyexchange.currency.model.Currency;
import java.util.List;
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
   * @return A list of currency codes (e.g., "USD", "EUR") as a {@link List} of {@link String}.
   */
  public List<String> getAllCurrencies() {
    log.info("Fetching all currencies from the repository.");
    List<Currency> currencies = repository.findAll();
    log.info("Found {} currencies.", currencies.size());
    return currencies.stream()
        .map(Currency::getCurrency)
        .toList();
  }
}
