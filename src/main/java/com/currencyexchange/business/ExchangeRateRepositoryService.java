package com.currencyexchange.business;

import com.currencyexchange.entity.ExchangeRateEntity;
import com.currencyexchange.repository.ExchangeRateRepository;
import java.math.BigDecimal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateRepositoryService {

  private final ExchangeRateRepository exchangeRateRepository;

  /**
   * Saves or updates exchange rates based on the provided data.
   *
   * @param ratesFromApi a map of exchange rates where the key is the base currency and the value is
   *     a map of target currencies and their rates
   */
  @Transactional
  public void saveOrUpdateCurrencyRates(Map<String, Map<String, BigDecimal>> ratesFromApi) {
    ratesFromApi.forEach(
        (baseCurrency, targetRates) ->
            targetRates.forEach(
                (targetCurrency, rate) ->
                    exchangeRateRepository
                        .findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency)
                        .ifPresentOrElse(
                            entity -> updateRateIfNeeded(entity, rate),
                            () -> saveNewRate(baseCurrency, targetCurrency, rate))));
  }

  private void updateRateIfNeeded(ExchangeRateEntity entity, BigDecimal newRate) {
    if (entity.getRate().compareTo(newRate) != 0) {
      entity.setRate(newRate);
      exchangeRateRepository.save(entity);
      log.debug(
          "Updated exchange rate for {} to {} {}",
          entity.getBaseCurrency(),
          newRate,
          entity.getTargetCurrency());
    }
  }

  private void saveNewRate(String baseCurrency, String targetCurrency, BigDecimal rate) {
    exchangeRateRepository.save(
        ExchangeRateEntity.builder()
            .baseCurrency(baseCurrency)
            .targetCurrency(targetCurrency)
            .rate(rate)
            .build());
    log.debug("Saved new exchange rate for {} to {}: {}", baseCurrency, targetCurrency, rate);
  }
}
