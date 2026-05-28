package com.currencyexchange.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.currencyexchange.business.CurrencyService;
import com.currencyexchange.controller.CurrencyController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    value = CurrencyController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class)
class GlobalExceptionHandlerCurrencyTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CurrencyService currencyService;

  @ParameterizedTest
  @ValueSource(strings = {"{\"currency\":\"\"}", "{\"currency\":\"GBPQ\"}",
      "{\"currency\":\"G\"}", "{\"currency\":\"!!!\"}", "{\"currency\":\"sde\"}",
      "{\"currency\":123}"})
  void addCurrency_shouldReturnBadRequestWhenCurrencyBodyInvalid(String body) throws Exception {
    mockMvc
        .perform(post("/api/v1/currencies").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.type").value("about:blank"))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.instance").value("/api/v1/currencies"))
        .andExpect(jsonPath("$.traceId").exists())
        .andExpect(jsonPath("$.errors[0].field").value("currency"))
        .andExpect(jsonPath("$.errors[0].message").value("Currency must be 3 uppercase letters"));
  }
}
