package com.currencyexchange.repository;

import java.math.BigDecimal;
import java.util.Map;

public interface ExchangeRateFetcher {
  /**
   * Fetches the latest exchange rates.
   *
   * @return a map of exchange rates, where the key is the base currency, and the value is a map of
   *     target currencies and their rates
   */
  Map<String, Map<String, BigDecimal>> getLatestRates();
}
