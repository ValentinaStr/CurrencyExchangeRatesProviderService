package com.currencyexchange.client;

import com.currencyexchange.model.Rates;
import java.util.Set;

public interface ExchangeRateClient {

  /**
   * Retrieves exchange rates for the given set of currencies.
   *
   * @param currency a set of currency codes for which exchange rates are requested
   * @return an {@link Rates} containing exchange rate data
   */
  Rates getExchangeRate(Set<String> currency);
}
