package com.currencyexchange.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.model.ExchangeRateEntity;
import com.currencyexchange.repository.ExchangeRateRepository;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExchangeRateRepositoryServiceTest {

  @Mock
  private ExchangeRateRepository exchangeRateRepository;

  @InjectMocks
  private ExchangeRateRepositoryService exchangeRateRepositoryService;

  @Test
  void saveOrUpdateCurrencyRates_shouldSaveNewRatesWhenCurrencyNotExist() {
    Map<String, Map<String, BigDecimal>> ratesFromApi =
        Map.of(
            "USW", Map.of("EUR", new BigDecimal("0.9")),
            "GBW", Map.of("USD", new BigDecimal("1.9")));
    when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrency("USW", "EUR"))
        .thenReturn(Optional.empty());
    when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrency("GBW", "USD"))
        .thenReturn(Optional.empty());

    exchangeRateRepositoryService.saveOrUpdateCurrencyRates(ratesFromApi);

    verify(exchangeRateRepository, times(2)).save(any(ExchangeRateEntity.class));
  }

  @Test
  void saveOrUpdateCurrencyRates_shouldUpdateRateWhenCurrencyExistsWithDifferentRate() {
    Map<String, Map<String, BigDecimal>> ratesFromApi =
        Map.of("USD", Map.of("EUR", new BigDecimal("0.85")));
    ExchangeRateEntity existingRate =
        new ExchangeRateEntity(null, "USD", "EUR", new BigDecimal("0.80"));
    when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrency("USD", "EUR"))
        .thenReturn(Optional.of(existingRate));

    exchangeRateRepositoryService.saveOrUpdateCurrencyRates(ratesFromApi);

    verify(exchangeRateRepository).save(existingRate);
  }

  @Test
  void saveOrUpdateCurrencyRates_shouldNotSaveWhenRatesAreSame() {
    Map<String, Map<String, BigDecimal>> ratesFromApi =
        Map.of("USD", Map.of("EUR", new BigDecimal("0.85")));
    ExchangeRateEntity existingRate =
        new ExchangeRateEntity(null, "USD", "EUR", new BigDecimal("0.85"));
    when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrency("USD", "EUR"))
        .thenReturn(Optional.of(existingRate));

    exchangeRateRepositoryService.saveOrUpdateCurrencyRates(ratesFromApi);

    verify(exchangeRateRepository, never()).save(any(ExchangeRateEntity.class));
  }
}
