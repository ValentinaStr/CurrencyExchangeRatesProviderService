package com.currencyexchange.cache;

import com.currencyexchange.exception.RateNotFoundInCacheException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExchangeRateCacheService {

  private final Map<String, Map<String, BigDecimal>> exchangeRatesCache = new ConcurrentHashMap<>();

  /**
   * Retrieves all exchange rates for a given currency.
   *
   * @param currency The currency for which to retrieve the exchange rates (e.g., "EUR").
   * @return A map of currency codes to exchange rates for the given currency.
   * @throws RateNotFoundInCacheException If rates for the currency are not found in the cache.
   */
  public Map<String, BigDecimal> getExchangeRates(String currency) {
    log.debug("Fetching exchange rates for currency: {}", currency);
    Map<String, BigDecimal> rates = exchangeRatesCache.get(currency);
    if (rates != null) {
      return rates;
    }

    throw new RateNotFoundInCacheException(
        "Exchange rates for currency " + currency + " not found in cache");
  }

  /**
   * Updates the cache with the provided exchange rates.
   *
   * @param rates A map where the key is the base currency, and the value is a map of target
   *     currencies with their exchange rates.
   */
  public void updateAll(Map<String, Map<String, BigDecimal>> rates) {
    rates.forEach(
        (currency, rate) -> {
          exchangeRatesCache.put(currency, rate);
          log.debug("Exchange rate for {} updated in cache: {}", currency, rate);
        });
  }
}
