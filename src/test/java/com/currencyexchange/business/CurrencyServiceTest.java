package com.currencyexchange.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.entity.CurrencyEntity;
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
  void getAllCurrencies_shouldReturnSetOfCurrencyCodes() {
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
  void addCurrency_shouldSaveCurrency() {
    currencyService.addCurrency("USD");

    verify(currencyRepository).save(new CurrencyEntity("USD"));
  }
}
