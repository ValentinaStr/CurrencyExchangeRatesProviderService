package com.currencyexchange.business;

import com.currencyexchange.cache.ExchangeRateCacheService;
import com.currencyexchange.provider.ExchangeRateProvider;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateUpdateService {

  private final List<ExchangeRateProvider> externalRateProvider;
  private final ExchangeRateCacheService currencyRateCacheService;
  private final ExchangeRateRepositoryService currencyRateRepositoryService;

  /** Fetches and updates exchange rates at startup and every hour thereafter. */
  @PostConstruct
  public void refreshRatesAtStart() {

    refreshRates();
  }

  /** Fetches and updates exchange rates every hour thereafter. */
  @Scheduled(fixedRate = 120000)
  @Transactional
  public void refreshRates() {
    log.info("Refreshing currency rates");
    for (ExchangeRateProvider provider : externalRateProvider) {
      Map<String, Map<String, BigDecimal>> ratesFromApi = provider.getLatestRates();
      currencyRateRepositoryService.saveOrUpdateCurrencyRates(ratesFromApi);
      currencyRateCacheService.saveRatesToCache(ratesFromApi);
    }
  }
}
