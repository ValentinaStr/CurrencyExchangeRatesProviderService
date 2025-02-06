package com.currencyexchange.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.model.CurrencyEntity;
import com.currencyexchange.repository.CurrencyRepository;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

  @Mock
  private CurrencyRepository currencyRepository;

  @InjectMocks
  private CurrencyService currencyService;

  @Test
  void getAllCurrencies_shouldReturnListOfCurrencyCodes() {
    when(currencyRepository.findAll())
        .thenReturn(List.of(new CurrencyEntity("USD"), new CurrencyEntity("EUR")));

    Set<String> result = currencyService.getAllCurrencies();

    assertThat(result).containsExactlyInAnyOrder("USD", "EUR");

    verify(currencyRepository).findAll();
  }

  @Test
  void getAllCurrencies_shouldReturnEmptyListWhenNoCurrencies() {
    when(currencyRepository.findAll()).thenReturn(Collections.emptyList());

    Set<String> result = currencyService.getAllCurrencies();

    assertThat(result).isEmpty();

    verify(currencyRepository).findAll();
  }

  @Test
  void getAllCurrencies_shouldHandleSingleCurrency() {
    List<CurrencyEntity> currencies = List.of(new CurrencyEntity("USD"));
    when(currencyRepository.findAll()).thenReturn(currencies);

    Set<String> result = currencyService.getAllCurrencies();

    assertThat(result).containsExactly("USD");

    verify(currencyRepository).findAll();
  }

  @Test
  void addCurrency_shouldSaveCurrency() {
    CurrencyEntity currency = new CurrencyEntity("USD");
    when(currencyRepository.save(currency)).thenReturn(currency);

    currencyService.addCurrency(currency);

    verify(currencyRepository).save(currency);
  }
}
