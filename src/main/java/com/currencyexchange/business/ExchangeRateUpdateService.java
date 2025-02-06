package com.currencyexchange.business;

import com.currencyexchange.provider.ExchangeRateProvider;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateUpdateService {

  private final List<ExchangeRateProvider> externalRateProvider;
  private final ExchangeRateRepositoryService currencyRateRepositoryService;

  /**
   * Updates currency rates and saving.
   */
  public void updateCurrencyRatesInDatabase() {
    log.info("Refreshing currency rates");
    for (ExchangeRateProvider provider : externalRateProvider) {
      Map<String, Map<String, BigDecimal>> ratesFromApi = provider.getLatestRates();
      currencyRateRepositoryService.saveOrUpdateCurrencyRates(ratesFromApi);
    }
  }
}
