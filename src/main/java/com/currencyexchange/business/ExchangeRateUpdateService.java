package com.currencyexchange.business;

import com.currencyexchange.cache.ExchangeRateCacheService;
import com.currencyexchange.repository.ExchangeRateFetcher;
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

  private final List<ExchangeRateFetcher> externalRateFetchers;
  private final ExchangeRateCacheService currencyRateCacheService;
  private final ExchangeRateRepositoryService currencyRateRepositoryService;

  /** Fetches and updates exchange rates at startup and every hour thereafter. */
  @PostConstruct
  @Scheduled(fixedRateString = "${scheduler.interval}")
  @Transactional
  public void refreshRates() {
    log.info("Refreshing currency rates...");
    for (ExchangeRateFetcher fetcher : externalRateFetchers) {
      Map<String, Map<String, BigDecimal>> ratesFromApi = fetcher.getLatestRates();
      currencyRateRepositoryService.saveOrUpdateCurrencyRates(ratesFromApi);
      currencyRateCacheService.saveRatesToCache(ratesFromApi);
    }
  }
}
