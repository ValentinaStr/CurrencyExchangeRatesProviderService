package com.currencyexchange.exception;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.currencyexchange.cache.ExchangeRateCacheService;
import com.currencyexchange.controller.ExchangeRateController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    value = ExchangeRateController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class)
class GlobalExceptionHandlerExchangeRateTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ExchangeRateCacheService exchangeRateCacheService;

  @Test
  void getExchangeRate_shouldReturnNotFoundWithProblemDetail() throws Exception {
    when(exchangeRateCacheService.getExchangeRates("PPP"))
        .thenThrow(
            new RateNotFoundInCacheException("Exchange rates for currency PPP not found in cache"));

    mockMvc
        .perform(get("/exchange-rates").param("currency", "PPP"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.type").value("about:blank"))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.title").value("Rate not found"))
        .andExpect(jsonPath("$.detail").value("Exchange rates for currency PPP not found in cache"))
        .andExpect(jsonPath("$.instance").value("/exchange-rates"))
        .andExpect(jsonPath("$.traceId").exists());
  }

  @ParameterizedTest
  @ValueSource(strings = {"854", "!!!", "", "US", "GWBP"})
  void getExchangeRate_shouldReturnBadRequestWhenCurrencyParamInvalid(String currency)
      throws Exception {
    mockMvc
        .perform(get("/exchange-rates").param("currency", currency))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.type").value("about:blank"))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.title").value("Validation failed"))
        .andExpect(jsonPath("$.instance").value("/exchange-rates"))
        .andExpect(jsonPath("$.traceId").exists())
        .andExpect(jsonPath("$.errors[0].message").value("Currency must be 3 uppercase letters"));
  }

  @Test
  void getExchangeRate_shouldReturnInternalServerErrorWithProblemDetail() throws Exception {
    when(exchangeRateCacheService.getExchangeRates("USD"))
        .thenThrow(new RuntimeException("Unexpected error"));

    mockMvc
        .perform(get("/exchange-rates").param("currency", "USD"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.type").value("about:blank"))
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.title").value("Internal server error"))
        .andExpect(jsonPath("$.detail").value("Internal server error"))
        .andExpect(jsonPath("$.instance").value("/exchange-rates"))
        .andExpect(jsonPath("$.traceId").exists());
  }
}
