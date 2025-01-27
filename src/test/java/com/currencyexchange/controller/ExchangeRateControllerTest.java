package com.currencyexchange.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.currencyexchange.business.ExchangeRateCacheService;
import com.currencyexchange.exception.RateNotFoundInCacheException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateControllerTest {

  @Mock
  private ExchangeRateCacheService exchangeRateService;

  @InjectMocks
  private ExchangeRateController exchangeRateController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(exchangeRateController).build();
  }

  @Test
  void testGetExchangeRate_whenCurrencyExists() throws Exception {
    when(exchangeRateService.getExchangeRate("USD")).thenReturn(1.25);

    mockMvc.perform(get("/exchange-rates")
            .param("currency", "USD"))
        .andExpect(status().isOk())
        .andExpect(content().string("Exchange rate for USD: 1.25"));
  }

  @Test
  void testGetExchangeRate_invalidCurrencyFormat_tooShortCode() throws Exception {
    mockMvc.perform(get("/exchange-rates")
            .param("currency", "US"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testGetExchangeRate_invalidCurrencyFormat_tooLongCode() throws Exception {
    mockMvc.perform(get("/exchange-rates")
            .param("currency", "UWWWS"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testGetExchangeRate_invalidCurrencyFormat_notAlphabetic() throws Exception {
    mockMvc.perform(get("/exchange-rates")
            .param("currency", "854"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testGetExchangeRate_invalidCurrencyFormat_emptyString() throws Exception {
    mockMvc.perform(get("/exchange-rates")
            .param("currency", ""))
        .andExpect(status().isBadRequest());
  }
}
