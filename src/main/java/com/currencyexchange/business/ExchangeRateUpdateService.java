package com.currencyexchange.business;

import com.currencyexchange.cache.ExchangeRateCacheService;
import java.math.BigDecimal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateUpdateService {

  private final RateService rateService;
  private final CurrencyService currencyService;
  private final ExchangeRateCacheService exchangeRateCacheService;
  private final ExchangeRateRepositoryService exchangeRateRepositoryService;

  public void refreshRates() {
    Map<String, Map<String, BigDecimal>> bestRates = rateService.getRates();

    if (!bestRates.isEmpty()) {
      exchangeRateRepositoryService.saveOrUpdateCurrencyRates(bestRates);
      exchangeRateCacheService.updateAll(bestRates);
      log.debug("Currency rates successfully refreshed and saved.");
    } else {
      log.warn("No exchange rates were updated.");
    }
  }
}
