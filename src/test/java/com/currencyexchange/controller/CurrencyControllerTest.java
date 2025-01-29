package com.currencyexchange.controller;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.currencyexchange.business.CurrencyService;
import com.currencyexchange.model.Currency;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class CurrencyControllerTest {

  @Mock
  private CurrencyService currencyService;

  @InjectMocks
  private CurrencyController currencyController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(currencyController).build();
  }

  @Test
  void getAllCurrencies_shouldReturnCurrencies() throws Exception {
    when(currencyService.getAllCurrencies()).thenReturn(Set.of("USD", "EUR"));

    mockMvc.perform(get("/api/v1/currencies/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasItems("USD", "EUR")));

    verify(currencyService, times(1)).getAllCurrencies();
  }

  @Test
  void getAllCurrencies_shouldReturnEmptyListWhenNoCurrencies() throws Exception {
    when(currencyService.getAllCurrencies()).thenReturn(Set.of());

    mockMvc.perform(get("/api/v1/currencies/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());

    verify(currencyService, times(1)).getAllCurrencies();
  }

  @Test
  void addCurrency_shouldSaveCurrency() throws Exception {
    Currency currencyValid = new Currency("GBP");
    doNothing().when(currencyService).addCurrency(currencyValid);

    mockMvc.perform(post("/api/v1/currencies/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"currency\":\"GBP\"}"))
        .andExpect(status().isCreated())
        .andExpect(content().string("Currency processed: GBP"));

    verify(currencyService, times(1)).addCurrency(currencyValid);
  }

  @Test
  void addCurrency_shouldReturnBadRequestWhenCurrencyIsEmpty() throws Exception {
    Currency currencyEmpty = new Currency("");

    mockMvc.perform(post("/api/v1/currencies/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"currency\":\"\"}"))
        .andExpect(status().isBadRequest());

    verify(currencyService, times(0)).addCurrency(currencyEmpty);
  }

  @Test
  void addCurrency_shouldReturnBadRequestWhenCurrencyIsNotAlphabetic() throws Exception {
    Currency currencyNotAlphabetic = new Currency("123");

    mockMvc.perform(post("/api/v1/currencies/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"currency\":\"123\"}"))
        .andExpect(status().isBadRequest());

    verify(currencyService, times(0)).addCurrency(currencyNotAlphabetic);
  }

  @Test
  void addCurrency_shouldReturnBadRequestWhenCurrencyTooLong() throws Exception {
    Currency currencyTooLong = new Currency("QWERT");

    mockMvc.perform(post("/api/v1/currencies/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"currency\":\"GBPQ\"}"))
        .andExpect(status().isBadRequest());

    verify(currencyService, times(0)).addCurrency(currencyTooLong);
  }

  @Test
  void addCurrency_shouldReturnBadRequestWhenCurrencyTooShort() throws Exception {
    Currency currencyTooShort = new Currency("Q");

    mockMvc.perform(post("/api/v1/currencies/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"currency\":\"G\"}"))
        .andExpect(status().isBadRequest());

    verify(currencyService, times(0)).addCurrency(currencyTooShort);
  }
}
