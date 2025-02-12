package com.currencyexchange.model;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Represents exchange rate information for a currency.
 *
 * @param currency The base currency.
 * @param rates Exchange rates relative to the base currency.
 */
public record ExchangeRateModel(String currency, Map<String, BigDecimal> rates) {}
