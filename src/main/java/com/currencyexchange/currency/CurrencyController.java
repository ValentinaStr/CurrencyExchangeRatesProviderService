package com.currencyexchange.currency;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

  private final CurrencyService currencyService;

  /**
   * Handles GET requests to retrieve a list of all available currencies.
   * This method logs the request and response, then returns the list of all currencies
   * from the currency service. The list is returned with a status code of 200 (OK).
   *
   * @return a {@link ResponseEntity} containing a list of currency names
   *     with an HTTP status code of 200 (OK)
   */
  @GetMapping
  public ResponseEntity<List<String>> getAllCurrencies() {
    log.info("Received request to get all currencies.");
    List<String> currencies = currencyService.getAllCurrencies();
    log.info("Returning list of currencies: {}", currencies);
    return ResponseEntity.ok(currencies);
  }
}
