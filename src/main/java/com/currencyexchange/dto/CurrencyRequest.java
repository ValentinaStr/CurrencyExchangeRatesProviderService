package com.currencyexchange.dto;

import jakarta.validation.constraints.Pattern;

public record CurrencyRequest(
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3 uppercase letters")
    String currency) {
}
