package com.currencyexchange.dto;

import com.currencyexchange.model.RatesModel;
import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO for exchange rate responses from the Fixer API.
 *
 * @param success Indicates whether the API request was successful.
 * @param timestamp Unix timestamp of the rate update.
 * @param base The base currency for the exchange rates.
 * @param rates A map of currency codes to their exchange rates.
 */
public record FixerDto(boolean success, long timestamp, String base, Map<String, BigDecimal> rates)
    implements ResponseDto {

  @Override
  public RatesModel toRates() {
    return new RatesModel(timestamp, base, rates);
  }
}
