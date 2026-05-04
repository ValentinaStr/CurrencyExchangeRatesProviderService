package com.currencyexchange.controller;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.currencyexchange.business.CurrencyService;
import com.currencyexchange.cache.ExchangeRateCacheService;
import com.currencyexchange.exception.RateNotFoundInCacheException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@WebMvcTest({ExchangeRateController.class, CurrencyController.class})
@WithMockUser(username = "user", password = "user123", roles = "USER")
class GlobalExceptionHandlerUserTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ExchangeRateCacheService exchangeRateCacheService;

  @MockitoBean
  private CurrencyService currencyService;

  @Test
  void getExchangeRate_shouldThrowRateNotFoundInCacheException() throws Exception {
    String errorMessage = "Exchange rates for currency PPP not found in cache";
    when(exchangeRateCacheService.getExchangeRates("PPP"))
        .thenThrow(new RateNotFoundInCacheException(errorMessage));
    mockMvc
        .perform(get("/exchange-rates/").param("currency", "PPP"))
        .andExpect(status().isNotFound())
        .andExpect(
            result ->
                assertInstanceOf(RateNotFoundInCacheException.class, result.getResolvedException()))
        .andExpect(jsonPath("$.error").value(errorMessage));
  }

  @Test
  void getExchangeRates_shouldValidationExceptionWhenCurrencyNumeric() throws Exception {
    mockMvc
        .perform(get("/exchange-rates/").param("currency", "854"))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertInstanceOf(
                    HandlerMethodValidationException.class, result.getResolvedException()))
        .andExpect(jsonPath("$.error").value("Currency must be 3 uppercase letters"));
  }

  @Test
  void getExchangeRates_shouldValidationExceptionWhenCurrencyInvalidSymbols() throws Exception {
    mockMvc
        .perform(get("/exchange-rates/").param("currency", "!!!"))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertInstanceOf(
                    HandlerMethodValidationException.class, result.getResolvedException()))
        .andExpect(jsonPath("$.error").value("Currency must be 3 uppercase letters"));
  }

  @Test
  void getExchangeRates_shouldValidationExceptionWhenCurrencyWhitespace() throws Exception {
    mockMvc
        .perform(get("/exchange-rates/").param("currency", ""))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertInstanceOf(
                    HandlerMethodValidationException.class, result.getResolvedException()))
        .andExpect(jsonPath("$.error").value("Currency must be 3 uppercase letters"));
  }

  @Test
  void getExchangeRates_shouldValidationExceptionWhenCurrencyTooShort() throws Exception {
    mockMvc
        .perform(get("/exchange-rates/").param("currency", "US"))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertInstanceOf(
                    HandlerMethodValidationException.class, result.getResolvedException()))
        .andExpect(jsonPath("$.error").value("Currency must be 3 uppercase letters"));
  }

  @Test
  void getExchangeRates_shouldMethodValidationExceptionWhenCurrencyTooLong() throws Exception {
    mockMvc
        .perform(get("/exchange-rates/").param("currency", "GWBP"))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertInstanceOf(
                    HandlerMethodValidationException.class, result.getResolvedException()))
        .andExpect(jsonPath("$.error").value("Currency must be 3 uppercase letters"));
  }

  @Test
  public void getExchangeRates_shouldReturn500AndMessage() throws Exception {
    mockMvc
        .perform(get("/exchange-rates/some-endpoint-that-causes-error"))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("Internal server error"));
  }

  @Test
  void addCurrency_shouldReturn500AndMessage() throws Exception {
    mockMvc
        .perform(get("/api/v1/currencies/some-endpoint-that-causes-error"))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("Internal server error"));
  }
}
