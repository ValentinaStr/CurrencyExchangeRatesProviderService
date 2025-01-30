package com.currencyexchange.business;

import com.currencyexchange.model.CurrencyEntity;
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
    List<CurrencyEntity> currencies = repository.findAll();
    log.info(
        "Found {} currencies: {}",
        currencies.size(),
        currencies.stream().map(CurrencyEntity::getCurrency).collect(Collectors.toList()));
    return currencies.stream().map(CurrencyEntity::getCurrency).collect(Collectors.toSet());
  }

  /**
   * Adds a new currency to the system by saving it to the repository.
   *
   * @param currency The {@link CurrencyEntity} object to be added.
   */
  public void addCurrency(CurrencyEntity currency) {
    log.info("Attempting to add currency: {}", currency.getCurrency());
    repository.save(currency);
    log.info("Currency added successfully: {}", currency.getCurrency());
  }
}
