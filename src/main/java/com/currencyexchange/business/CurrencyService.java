package com.currencyexchange.business;

import com.currencyexchange.entity.CurrencyEntity;
import com.currencyexchange.repository.CurrencyRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CurrencyService {

  private final CurrencyRepository repository;

  /**
   * Retrieves all currency codes available in the repository.
   *
   * @return a set of currency codes (e.g., "USD", "EUR")
   */
  @Transactional(readOnly = true)
  public Set<String> getAllCurrencies() {
    log.debug("Fetching all currencies from the repository.");
    List<CurrencyEntity> currencies = repository.findAll();
    log.debug("Found {} currencies", currencies.size());
    return currencies.stream().map(CurrencyEntity::getCurrency).collect(Collectors.toSet());
  }

  /**
   * Adds a new currency to the system by saving it to the repository.
   *
   * @param currency the currency code to be added (e.g., "USD")
   */
  @Transactional
  public void addCurrency(String currency) {
    log.debug("Adding currency: {}", currency);
    repository.save(new CurrencyEntity(currency));
    log.debug("Currency added successfully: {}", currency);
  }
}
