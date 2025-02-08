package com.currencyexchange.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO for exchange rate responses.
 *
 * @param success Indicates if the request was successful.
 * @param timestamp Unix timestamp of the rate update.
 * @param base Base currency for the exchange rates.
 * @param rates Map of currency codes to exchange rates.
 */
public record ExchangeRateResponseDto(
    boolean success, long timestamp, String base, Map<String, BigDecimal> rates) {}
