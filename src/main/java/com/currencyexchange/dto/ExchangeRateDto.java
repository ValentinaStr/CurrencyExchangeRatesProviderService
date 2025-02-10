package com.currencyexchange.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO (Data Transfer Object) for representing currency exchange rate information. It contains a
 * currency and the corresponding exchange rates for other currencies.
 *
 * @param currency The currency for which exchange rates are provided.
 * @param rates A map where keys are currency codes and values are the exchange rates relative to
 *     the {@code currency}.
 */
public record ExchangeRateDto(String currency, Map<String, BigDecimal> rates) {}
