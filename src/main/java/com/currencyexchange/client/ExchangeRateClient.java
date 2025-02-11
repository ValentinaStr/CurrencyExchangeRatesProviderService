package com.currencyexchange.client;

import com.currencyexchange.model.RatesModel;
import java.util.Set;

public interface ExchangeRateClient {

  /**
   * Retrieves exchange rates for the given set of currencies.
   *
   * @param currency a set of currency codes for which exchange rates are requested
   * @return an {@link RatesModel} containing exchange rate data
   */
  RatesModel getExchangeRate(Set<String> currency);
}
