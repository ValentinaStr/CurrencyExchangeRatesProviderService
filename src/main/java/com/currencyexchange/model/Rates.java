package com.currencyexchange.model;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO for exchange rate responses.
 *
 * @param timestamp Unix timestamp of the rate update.
 * @param base Base currency for the exchange rates.
 * @param rates Map of currency codes to exchange rates.
 */
public record Rates(long timestamp, String base, Map<String, BigDecimal> rates) {}
