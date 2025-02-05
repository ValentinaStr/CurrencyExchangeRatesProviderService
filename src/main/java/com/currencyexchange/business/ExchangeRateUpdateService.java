package com.currencyexchange.business;

import static java.math.BigDecimal.ZERO;

import com.currencyexchange.cache.ExchangeRateCacheService;
import com.currencyexchange.provider.ExchangeRateProvider;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateUpdateService  // implements ApplicationRunner
 {

  private final CurrencyService currencyService;
  private final List<ExchangeRateProvider> externalRateProvider;
  private final ExchangeRateCacheService currencyRateCacheService;
  private final ExchangeRateRepositoryService currencyRateRepositoryService;

  /* @Override
  public void run(ApplicationArguments args) {
    refreshRates();
  }
*/
  /** Fetches and updates exchange rates at startup and every hour thereafter. */
  /* @PostConstruct
  public void refreshRatesAtStart() throws IOException {
    refreshRates();
  }*/

  /** Fetches and updates exchange rates every hour thereafter. */
  @Scheduled(fixedRate = 3600000)
  public Map<String, Map<String, BigDecimal>> refreshRates() {
    Set<String> currency = currencyService.getAllCurrencies();
    Map<String, Map<String, BigDecimal>> bestRates = new HashMap<>();

    for (ExchangeRateProvider provider : externalRateProvider) {
      Map<String, Map<String, BigDecimal>> ratesFromApi = provider.getLatestRates(currency);
      updateBestRates(bestRates, ratesFromApi);
    }
    currencyRateRepositoryService.saveOrUpdateCurrencyRates(bestRates);
    currencyRateCacheService.save(bestRates);

    log.info("Currency rates successfully refreshed and saved.");
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
        if (newRate.compareTo(bestRates.get(baseCurrency).getOrDefault(targetCurrency, ZERO)) > 0) {
          bestRates.get(baseCurrency).put(targetCurrency, newRate);
        }
      }
    }
  }
}
