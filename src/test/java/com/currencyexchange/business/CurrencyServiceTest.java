package com.currencyexchange.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.currencyexchange.model.Currency;
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
public class CurrencyServiceTest {

  @Mock
  private CurrencyRepository currencyRepository;

  @InjectMocks
  private CurrencyService currencyService;

  @Test
  void testGetAllCurrencies_shouldReturnListOfCurrencyCodes() {
    when(currencyRepository.findAll()).thenReturn(List.of(
        new Currency( "USD"), new Currency("EUR")));
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
    List<Currency> currencies = List.of(new Currency("USD"));
    when(currencyRepository.findAll()).thenReturn(currencies);
    Set<String> result = currencyService.getAllCurrencies();

    assertThat(result).containsExactly("USD");
  }

  @Test
  void testAddCurrency_shouldSaveCurrency() {
    Currency currency = new Currency("USD");
    when(currencyRepository.save(currency)).thenReturn(currency);
    currencyService.addCurrency(currency);
    verify(currencyRepository, times(1)).save(currency);
  }
}
