package com.currencyexchange.business;

import com.currencyexchange.cache.ExchangeRateCacheService;
import com.currencyexchange.provider.ExchangeRateProvider;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateUpdateService {

  private final CurrencyService currencyService;
  private final List<ExchangeRateProvider> externalRateProvider;
  private final ExchangeRateCacheService currencyRateCacheService;
  private final ExchangeRateRepositoryService currencyRateRepositoryService;

  /** Fetches and updates exchange rates at startup and every hour thereafter. */
  @PostConstruct
  public void refreshRatesAtStart() {

    refreshRates();
  }

  /** Fetches and updates exchange rates every hour thereafter. */
  @Scheduled(fixedRate = 3600000)
  public Map<String, Map<String, BigDecimal>> refreshRates() {
    log.info("Fetching latest rates.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!..");
    Set<String> currency = currencyService.getAllCurrencies();

    Map<String, Map<String, BigDecimal>> bestRates = new HashMap<>();

    log.info("Refreshing currency rates");
    for (ExchangeRateProvider provider : externalRateProvider) {
      Map<String, Map<String, BigDecimal>> ratesFromApi = provider.getLatestRates(currency);

      updateBestRates(bestRates, ratesFromApi);
    }

    currencyRateRepositoryService.saveOrUpdateCurrencyRates(bestRates);
    currencyRateCacheService.saveRatesToCache(bestRates);

    return bestRates;
  }

  private void updateBestRates(
      Map<String, Map<String, BigDecimal>> bestRates,
      Map<String, Map<String, BigDecimal>> ratesFromApi) {

    for (String baseCurrency : ratesFromApi.keySet()) {
      Map<String, BigDecimal> rates = ratesFromApi.get(baseCurrency);

      if (!bestRates.containsKey(baseCurrency)) {
        bestRates.put(baseCurrency, new HashMap<>());
      }

      for (String targetCurrency : rates.keySet()) {
        BigDecimal newRate = rates.get(targetCurrency);

        if (newRate.compareTo(
                bestRates.get(baseCurrency).getOrDefault(targetCurrency, BigDecimal.ZERO))
            > 0) {
          bestRates.get(baseCurrency).put(targetCurrency, newRate);
        }
      }
    }
  }
}
