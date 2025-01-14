package com.currencyexchange.currency;

import com.currencyexchange.currency.model.Currency;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {
  private final CurrencyRepository repository;

  public CurrencyService(CurrencyRepository repository) {
    this.repository = repository;
  }

  public List<Currency> getAllCurrencies() {
    return repository.findAll();
  }
}
