package com.currencyexchange.dto;

import java.util.Set;

/**
 * DTO (Data Transfer Object) for representing a list of available currencies. This is used as a
 * response object when retrieving all supported currencies.
 *
 * @param currencies A set containing currency codes (e.g., "USD", "EUR", "GBP").
 */
public record CurrencyListDto(Set<String> currencies) {}
