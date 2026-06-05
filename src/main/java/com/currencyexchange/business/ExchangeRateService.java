package com.currencyexchange.business;

import com.currencyexchange.cache.ExchangeRateCacheService;
import com.currencyexchange.exception.RateNotFoundInCacheException;
import java.math.BigDecimal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateService {

  private final ExchangeRateCacheService exchangeRateCacheService;
  private final ExchangeRateRepositoryService exchangeRateRepositoryService;

  /**
   * Retrieves exchange rates for a given currency. Returns rates from cache if available,
   * falls back to the database otherwise.
   *
   * @param currency the base currency code (e.g., "GBP")
   * @return a map of target currency codes to their exchange rates
   * @throws RateNotFoundInCacheException if rates are not found in cache or database
   */
  public Map<String, BigDecimal> getExchangeRates(String currency) {
    try {
      return exchangeRateCacheService.getExchangeRates(currency);
    } catch (RateNotFoundInCacheException e) {
      log.warn("Cache miss for {}. Falling back to database.", currency);
      Map<String, BigDecimal> rates = exchangeRateRepositoryService.findRatesByBaseCurrency(currency);
      if (rates.isEmpty()) {
        throw e;
      }
      return rates;
    }
  }
}
