package com.currencyexchange.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO (Data Transfer Object) для представления информации о курсах валют. Содержит валюту и
 * соответствующие ей курсы для других валют.
 *
 * @param currency Валюта, для которой получены курсы.
 * @param rates Карта, где ключи — это коды валют, а значения — их курсы относительно валюты {@code
 *     currency}.
 */
public record ExchangeRateDto(String currency, Map<String, BigDecimal> rates) {}
