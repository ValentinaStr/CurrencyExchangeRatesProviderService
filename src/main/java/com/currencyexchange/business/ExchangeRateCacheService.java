package com.currencyexchange.business;

import com.currencyexchange.exception.RateNotFoundInCacheException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExchangeRateCacheService {

  private final Map<String, Double> exchangeRatesCache = new HashMap<>();

  /**
   * Constructor that initializes the cache with some predefined exchange rates.
   * Currently, this includes rates for EUR, USD, GBP, and JPY.
   */
  public ExchangeRateCacheService() {
    exchangeRatesCache.put("EUR", 1.0);
    exchangeRatesCache.put("USD", 1.087);
    exchangeRatesCache.put("GBP", 1.2);
    exchangeRatesCache.put("JPY", 0.0074);

    log.info("Exchange rate cache initialized with predefined rates:"
        + " EUR=1.0, USD=1.087, GBP=1.2, JPY=0.0074");
  }

  /**
   * Retrieves the exchange rate for the given currency from the cache.
   *
   * @param currency The name of the currency (e.g., "USD", "GBP").
   * @return The exchange rate of the specified currency relative to EUR.
   * @throws RateNotFoundInCacheException
   *     If the exchange rate for the given currency is not found in the cache.
   */
  public Double getExchangeRate(String currency) throws RateNotFoundInCacheException {
    log.info("Request to get exchange rate for currency: {}", currency);
    if (exchangeRatesCache.containsKey(currency)) {
      var exchangeRate = exchangeRatesCache.get(currency);
      log.info("Exchange rate for {} found: {}", currency, exchangeRate);
      return exchangeRate;
    }
    throw new RateNotFoundInCacheException("Exchange rate for " + currency + " not found in cache");
  }
}
