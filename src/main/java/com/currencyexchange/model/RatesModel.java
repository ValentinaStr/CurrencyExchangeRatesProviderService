package com.currencyexchange.model;

import java.math.BigDecimal;
import java.util.Map;
import lombok.Builder;

/**
 * Model representing exchange rate data.
 *
 * @param timestamp Unix timestamp of the rate update.
 * @param base Base currency for the exchange rates.
 * @param rates Map of currency codes to exchange rates.
 */
@Builder
public record RatesModel(long timestamp, String base, Map<String, BigDecimal> rates) {}
