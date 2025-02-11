package com.currencyexchange.model;

import java.util.Set;

/**
 * Represents a model containing a list of available currencies.
 *
 * <p>This model is used to store and retrieve supported currency codes.
 *
 * @param currencies A set of currency codes (e.g., "USD", "EUR", "GBP").
 */
public record CurrencyListModel(Set<String> currencies) {}
