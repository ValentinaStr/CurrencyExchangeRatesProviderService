package com.currencyexchange.business;

import com.currencyexchange.model.ExchangeRateEntity;
import com.currencyexchange.repository.ExchangeRateRepository;
import java.math.BigDecimal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateRepositoryService {
  private ExchangeRateRepository exchangeRateRepository;

  /**
   * Saves or updates exchange rates based on the provided data.
   *
   * @param ratesFromApi a map of exchange rates where the key is the base currency and the value is
   *     a map of target currencies and their rates
   */
  public void saveOrUpdateCurrencyRates(Map<String, Map<String, BigDecimal>> ratesFromApi) {
    ratesFromApi.forEach(
        (baseCurrency, targetRates) ->
            targetRates.forEach(
                (targetCurrency, rate) ->
                    exchangeRateRepository
                        .findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency)
                        .map(entity -> updateRateIfNeeded(entity, rate))
                        .orElseGet(() -> saveNewRate(baseCurrency, targetCurrency, rate))));
  }

  private ExchangeRateEntity updateRateIfNeeded(ExchangeRateEntity entity, BigDecimal newRate) {
    if (entity.getRate().compareTo(newRate) != 0) {
      entity.setRate(newRate);
      exchangeRateRepository.save(entity);
      log.info(
          "Updated exchange rate for {} to {} {}",
          entity.getBaseCurrency(),
          newRate,
          entity.getTargetCurrency());
    }
    return entity;
  }

  private ExchangeRateEntity saveNewRate(
      String baseCurrency, String targetCurrency, BigDecimal rate) {
    ExchangeRateEntity savedEntity =
        exchangeRateRepository.save(
            ExchangeRateEntity.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(rate)
                .build());
    log.info("Saved new exchange rate for {} to {}: {}", baseCurrency, targetCurrency, rate);
    return savedEntity;
  }
}
