package com.currencyexchange.currency;

import com.currencyexchange.currency.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/currencies")
public class CurrencyController {
  private final CurrencyService currencyService;

  @Autowired
  public CurrencyController(CurrencyService currencyService) {
    this.currencyService = currencyService;
  }

  @GetMapping
  public ResponseEntity<List<Currency>> getAllCurrencies() {
    List<Currency> currencies = currencyService.getAllCurrencies();
    return ResponseEntity.ok(currencies);
  }
}
