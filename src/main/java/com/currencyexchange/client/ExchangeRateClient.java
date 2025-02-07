package com.currencyexchange.client;

import com.currencyexchange.dto.ExchangeRateResponseDto;
import java.util.Set;

public interface ExchangeRateClient {

  /**
   * Retrieves exchange rates for the given set of currencies.
   *
   * @param currency a set of currency codes for which exchange rates are requested
   * @return an {@link ExchangeRateResponseDto} containing exchange rate data
   */
  ExchangeRateResponseDto getExchangeRate(Set<String> currency);
}
