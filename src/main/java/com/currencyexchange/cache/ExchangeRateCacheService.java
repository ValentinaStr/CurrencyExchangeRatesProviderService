package com.currencyexchange.cache;

import com.currencyexchange.exception.RateNotFoundInCacheException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExchangeRateCacheService {

  private final Map<String, Map<String, Double>> exchangeRatesCache = new HashMap<>();

  /**
   * Constructor that initializes the cache with some predefined exchange rates.
   * Currently, this includes rates for EUR, USD and GBP relative to other currencies.
   */
  public ExchangeRateCacheService() {
    Map<String, Double> eur = new HashMap<>();
    eur.put("EUR", 1.0);
    eur.put("USD", 1.087);
    eur.put("GBP", 0.85);

    Map<String, Double> usd = new HashMap<>();
    usd.put("EUR", 0.92);
    usd.put("USD", 1.0);
    usd.put("GBP", 0.78);

    Map<String, Double> gbp = new HashMap<>();
    gbp.put("EUR", 1.18);
    gbp.put("USD", 1.28);
    gbp.put("GBP", 1.0);

    exchangeRatesCache.put("EUR", eur);
    exchangeRatesCache.put("USD", usd);
    exchangeRatesCache.put("GBP", gbp);

    log.info("Exchange rate cache initialized with predefined rates for EUR, USD, GBP.");
  }

  /**
   * Retrieves all exchange rates for a given currency.
   *
   * @param currency The currency for which to retrieve the exchange rates (e.g., "EUR").
   * @return A map of currency codes to exchange rates for the given currency.
   * @throws RateNotFoundInCacheException If rates for the currency are not found in the cache.
   */
  public Map<String, Double> getExchangeRates(String currency) {
    if (exchangeRatesCache.containsKey(currency)) {
      return exchangeRatesCache.get(currency);
    }
    throw new RateNotFoundInCacheException(
        "Exchange rates for currency " + currency + " not found in cache");
  }
}
