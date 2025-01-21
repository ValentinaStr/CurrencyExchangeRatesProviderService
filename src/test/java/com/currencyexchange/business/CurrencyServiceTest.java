package com.currencyexchange.currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.currencyexchange.business.CurrencyService;
import com.currencyexchange.model.Currency;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.currencyexchange.repo.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

  @Mock
  private CurrencyRepository currencyRepository;

  @InjectMocks
  private CurrencyService currencyService;

  @Test
  void testGetAllCurrencies_shouldReturnListOfCurrencyCodes() {
    when(currencyRepository.findAll()).thenReturn(List.of(new Currency(null, "USD"), new Currency(null, "EUR")));
    Set<String> result = currencyService.getAllCurrencies();

    assertThat(result).containsExactlyInAnyOrder("USD", "EUR");
  }

  @Test
  void testGetAllCurrencies_shouldReturnEmptyListWhenNoCurrencies() {
    when(currencyRepository.findAll()).thenReturn(Collections.emptyList());
    Set<String> result = currencyService.getAllCurrencies();

    assertThat(result).isEmpty();
  }

  @Test
  void testGetAllCurrencies_shouldHandleSingleCurrency() {

    List<Currency> currencies = List.of(new Currency(null, "USD"));
    when(currencyRepository.findAll()).thenReturn(currencies);
    Set<String> result = currencyService.getAllCurrencies();

    assertThat(result).containsExactly("USD");
  }
}
