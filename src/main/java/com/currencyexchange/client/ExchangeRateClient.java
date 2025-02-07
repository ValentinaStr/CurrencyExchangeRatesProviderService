package com.currencyexchange.client;

import com.currencyexchange.dto.ExchangeRateResponseDto;
import java.util.Set;

public interface ExchangeRateClient {

  ExchangeRateResponseDto getExchangeRate(Set<String> currency);
}
