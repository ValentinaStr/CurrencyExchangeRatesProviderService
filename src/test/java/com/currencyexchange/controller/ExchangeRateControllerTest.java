package com.currencyexchange.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.currencyexchange.cache.ExchangeRateCacheService;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ExchangeRateControllerTest {

  @Mock private ExchangeRateCacheService exchangeRateCacheService;

  @InjectMocks private ExchangeRateController exchangeRateController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(exchangeRateController).build();
  }

  @Test
  void getExchangeRate_shouldReturnRates() throws Exception {
    Map<String, BigDecimal> mockExchangeRates =
        Map.of(
            "EUR", new BigDecimal("1.18"),
            "GBP", new BigDecimal("1.0"),
            "USD", new BigDecimal("1.28"));

    when(exchangeRateCacheService.getExchangeRates("GBP")).thenReturn(mockExchangeRates);

    mockMvc
        .perform(get("/exchange-rates/").param("currency", "GBP"))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    """
                {
                  "EUR": 1.18,
                  "GBP": 1.0,
                  "USD": 1.28
                }
            """));

    verify(exchangeRateCacheService, times(1)).getExchangeRates("GBP");
  }

  @Test
  void getExchangeRate_shouldReturnBadRequestWhenCurrencyCodeTooShort() throws Exception {
    mockMvc
        .perform(get("/exchange-rates/").param("currency", "US"))
        .andExpect(status().isBadRequest());

    verify(exchangeRateCacheService, times(0)).getExchangeRates("US");
  }

  @Test
  void getExchangeRate_shouldReturnBadRequestWhenCurrencyCodeTooLong() throws Exception {
    mockMvc
        .perform(get("/exchange-rates/").param("currency", "UWWWS"))
        .andExpect(status().isBadRequest());

    verify(exchangeRateCacheService, times(0)).getExchangeRates("UWWWS");
  }

  @Test
  void getExchangeRate_shouldReturnBadRequestWhenCurrencyCodeNotAlphabetic() throws Exception {
    mockMvc
        .perform(get("/exchange-rates/").param("currency", "854"))
        .andExpect(status().isBadRequest());

    verify(exchangeRateCacheService, times(0)).getExchangeRates("854");
  }

  @Test
  void getExchangeRate_shouldReturnBadRequestWhenCurrencyCodeIsEmpty() throws Exception {
    mockMvc
        .perform(get("/exchange-rates/").param("currency", ""))
        .andExpect(status().isBadRequest());

    verify(exchangeRateCacheService, times(0)).getExchangeRates("");
  }
}
