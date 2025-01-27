package com.currencyexchange.business;

import com.currencyexchange.exception.RateNotFoundInCacheException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateCacheService {

  private final Map<String, Double> exchangeRatesCache = new HashMap<>();

  /**
   * Adds or updates the exchange rate for a specified currency in the cache.
   *
   * @param currency The 3-letter code of the currency (e.g., "USD", "EUR")
   * @param rate     The exchange rate as a {@code Double} to be added or updated.
   */
  public void addExchangeRate(String currency, Double rate) {
    exchangeRatesCache.put(currency, rate);
  }

  /**
   * Retrieves the exchange rate for the specified currency from the cache.
   * If the currency is found in the cache, returns its corresponding exchange rate.
   * Otherwise, throws an {@link RateNotFoundInCacheException}.
   *
   * @param currency The 3-letter code of the currency (e.g., "USD", "EUR")
   * @return The exchange rate as a {@code Double} for the specified currency if found in the cache.
   * @throws RateNotFoundInCacheException if the exchange rate for the currency is not found.
   */
  public Double getExchangeRate(String currency) throws RateNotFoundInCacheException {
    if (exchangeRatesCache.containsKey(currency)) {
      return exchangeRatesCache.get(currency);
    }
    throw new RateNotFoundInCacheException("Exchange rate for " + currency + " not found in cache");
  }

  /**
   * Clears all entries in the exchange rates cache.
   */
  public void clearCache() {
    exchangeRatesCache.clear();
  }
}
