package com.currencyexchange.business;

import com.currencyexchange.cache.ExchangeRateCacheService;
import java.math.BigDecimal;
import java.util.Map;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class ExchangeRateUpdateService implements ApplicationRunner {

  private final RateService exchangeRateUpdateService;
  private final CurrencyService currencyService;
  private final ExchangeRateCacheService exchangeRateCacheService;
  private final ExchangeRateRepositoryService exchangeRateRepositoryService;

  /** Fetches and updates exchange rates at startup and every hour thereafter. */
  @Override
  public void run(ApplicationArguments args) {
    refreshRates();
  }

  /** Fetches and updates exchange rates every hour thereafter. */
  @Scheduled(fixedRate = 3600000)
  public void refreshRates() {

    Map<String, Map<String, BigDecimal>> bestRates = exchangeRateUpdateService.refreshRates();

    if (!bestRates.isEmpty()) {
      exchangeRateRepositoryService.saveOrUpdateCurrencyRates(bestRates);
      exchangeRateCacheService.save(bestRates);

      log.info("Currency rates successfully refreshed and saved.");
    } else {
      log.warn("No exchange rates were updated.");
    }
  }
}
