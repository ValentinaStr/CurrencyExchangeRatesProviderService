package com.currencyexchange.currency;
import com.currencyexchange.currency.model.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyControllerTest {

  @Mock
  private CurrencyService currencyService;

  @InjectMocks
  private CurrencyController currencyController;

  private List<Currency> mockCurrencies;

  @BeforeEach
  void setUp() {
    mockCurrencies = Arrays.asList(
        new Currency(1L, "USD"),
        new Currency(2L, "EUR")
    );
  }

  @Test
  void testGetAllCurrencies() {
    when(currencyService.getAllCurrencies()).thenReturn(mockCurrencies);

    ResponseEntity<List<Currency>> response = currencyController.getAllCurrencies();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(mockCurrencies);
  }

  @Test
  void testGetAllCurrencies_empty() {

    when(currencyService.getAllCurrencies()).thenReturn(List.of());

    ResponseEntity<List<Currency>> response = currencyController.getAllCurrencies();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEmpty();
  }
}
