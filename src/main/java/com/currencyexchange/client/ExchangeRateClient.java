package com.currencyexchange.client;

import com.currencyexchange.model.RatesModel;

public interface ExchangeRateClient {

  /**
   * Retrieves exchange rates for the given base currency.
   *
   * @param baseCurrency the base currency code (e.g., "USD", "EUR")
   * @return a {@link RatesModel} containing exchange rate data, or null if unavailable
   */
  RatesModel getExchangeRate(String baseCurrency);
}
