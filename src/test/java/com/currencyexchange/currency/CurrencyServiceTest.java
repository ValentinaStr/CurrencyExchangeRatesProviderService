package com.currencyexchange.currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.currencyexchange.currency.model.Currency;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class for the {@link CurrencyService}.
 * This class contains unit tests for the methods of the {@link CurrencyService} class,
 * particularly the {@link CurrencyService#getAllCurrencies()} method.
 * The tests verify the behavior of the service when retrieving all currencies from the repository.
 */
@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

  @Mock
  private CurrencyRepository currencyRepository;

  @InjectMocks
  private CurrencyService currencyService;

  @Test
  void testGetAllCurrencies_shouldReturnListOfCurrencyCodes() {

    Currency currency1 = new Currency(1L, "USD");
    Currency currency2 = new Currency(2L, "EUR");

    when(currencyRepository.findAll()).thenReturn(List.of(currency1, currency2));
    List<String> result = currencyService.getAllCurrencies();
    assertThat(result).containsExactly("USD", "EUR");
  }

  @Test
  void testGetAllCurrencies_shouldReturnEmptyListWhenNoCurrencies() {

    when(currencyRepository.findAll()).thenReturn(Collections.emptyList());
    List<String> result = currencyService.getAllCurrencies();
    assertThat(result).isEmpty();
  }

  @Test
  void testGetAllCurrencies_shouldHandleSingleCurrency() {

    List<Currency> currencies = Collections.singletonList(new Currency(1L, "USD"));
    when(currencyRepository.findAll()).thenReturn(currencies);
    List<String> result = currencyService.getAllCurrencies();
    assertThat(result).containsExactly("USD");
  }
}
