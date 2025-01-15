package com.currencyexchange.currency;

import com.currencyexchange.currency.model.Currency;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyService {
  private final CurrencyRepository repository;

  public CurrencyService(CurrencyRepository repository) {
    this.repository = repository;
  }

  public List<String> getAllCurrencies() {
    List<Currency> currencies = repository.findAll();
    return currencies.stream()
        .map(Currency::getCurrency)
        .collect(Collectors.toList());
  }
}
