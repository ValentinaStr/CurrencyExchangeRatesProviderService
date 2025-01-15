package com.currencyexchange.currency;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing currencies.
 * Provides endpoints to interact with currency data.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

  private final CurrencyService currencyService;

  /**
   * Endpoint to retrieve the list of all available currencies.
   *
   * @return A list of currency codes (e.g., "USD", "EUR") as a JSON array.
   */
  @GetMapping
  public ResponseEntity<List<String>> getAllCurrencies() {
    log.info("Received request to get all currencies.");
    List<String> currencies = currencyService.getAllCurrencies();
    log.info("Returning list of currencies: {}", currencies);
    return ResponseEntity.ok(currencies);
  }
}
