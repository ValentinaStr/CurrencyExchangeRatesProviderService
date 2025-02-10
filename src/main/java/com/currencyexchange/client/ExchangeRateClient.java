package com.currencyexchange.client;

import com.currencyexchange.dto.FixerDto;
import java.util.Set;

public interface ExchangeRateClient {

  /**
   * Retrieves exchange rates for the given set of currencies.
   *
   * @param currency a set of currency codes for which exchange rates are requested
   * @return an {@link FixerDto} containing exchange rate data
   */
  FixerDto getExchangeRate(Set<String> currency);
}
