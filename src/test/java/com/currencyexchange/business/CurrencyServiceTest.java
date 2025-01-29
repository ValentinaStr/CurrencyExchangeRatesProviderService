package com.currencyexchange.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.model.CurrencyCode;
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
    when(currencyRepository.findAll()).thenReturn(
        List.of(new CurrencyCode("USD"), new CurrencyCode("EUR")));

    Set<String> result = currencyService.getAllCurrencies();

    assertThat(result).containsExactlyInAnyOrder("USD", "EUR");
  }

  @Test
  void getAllCurrencies_shouldReturnEmptyListWhenNoCurrencies() {
    when(currencyRepository.findAll()).thenReturn(Collections.emptyList());

    Set<String> result = currencyService.getAllCurrencies();

    assertThat(result).isEmpty();
  }

  @Test
  void getAllCurrencies_shouldHandleSingleCurrency() {
    List<CurrencyCode> currencies = List.of(new CurrencyCode("USD"));
    when(currencyRepository.findAll()).thenReturn(currencies);

    Set<String> result = currencyService.getAllCurrencies();

    assertThat(result).containsExactly("USD");
  }

  @Test
  void addCurrency_shouldSaveCurrency() {
    CurrencyCode currency = new CurrencyCode("USD");
    when(currencyRepository.save(currency)).thenReturn(currency);

    currencyService.addCurrency(currency);

    verify(currencyRepository, times(1)).save(currency);
  }
}
