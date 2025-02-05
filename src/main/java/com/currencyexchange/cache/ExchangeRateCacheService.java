package com.currencyexchange.cache;

import com.currencyexchange.exception.RateNotFoundInCacheException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExchangeRateCacheService {

  private final Map<String, Map<String, BigDecimal>> exchangeRatesCache = new HashMap<>();

  /**
   * Constructor that initializes the cache with some predefined exchange rates. Currently, this
   * includes rates for EUR, USD and GBP relative to other currencies.
   */
  public ExchangeRateCacheService() {
    Map<String, BigDecimal> eur = new HashMap<>();
    eur.put("USD", new BigDecimal("0.87"));
    eur.put("GBP", new BigDecimal("0.85"));

    Map<String, BigDecimal> usd = new HashMap<>();
    usd.put("EUR", new BigDecimal("0.92"));
    usd.put("GBP", new BigDecimal("0.78"));

    Map<String, BigDecimal> gbp = new HashMap<>();
    gbp.put("EUR", new BigDecimal("1.18"));
    gbp.put("USD", new BigDecimal("1.28"));

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
  public Map<String, BigDecimal> getExchangeRates(String currency) {
    log.info("Fetching exchange rates for currency: {}", currency);
    if (exchangeRatesCache.containsKey(currency)) {
      return exchangeRatesCache.get(currency);
    }

    throw new RateNotFoundInCacheException(
        "Exchange rates for currency " + currency + " not found in cache");
  }

  /**
   * Updates the cache with the provided exchange rates.
   *
   * @param allRates A map where the key is the base currency, and the value is a map of target
   *     currencies with their exchange rates.
   */
  public void saveRatesToCache(Map<String, Map<String, BigDecimal>> allRates) {
    allRates.forEach(
        (currency, rate) -> {
          exchangeRatesCache.put(currency, rate);
          log.info("Exchange rate for {} updated in cache: {}", currency, rate);
        });
  }
}
