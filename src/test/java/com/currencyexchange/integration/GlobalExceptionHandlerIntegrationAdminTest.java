package com.currencyexchange.integration;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.currencyexchange.exception.RateNotFoundInCacheException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", password = "admin123", roles = "ADMIN")
public class GlobalExceptionHandlerIntegrationAdminTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getExchangeRate_shouldThrowRateNotFoundInCacheException() throws Exception {
    String errorMessage = "Exchange rates for currency PPP not found in cache";
    mockMvc
        .perform(get("/exchange-rates/").param("currency", "PPP"))
        .andExpect(status().isNotFound()).andExpect(
            result ->
                assertInstanceOf(RateNotFoundInCacheException.class, result.getResolvedException()))
        .andExpect(jsonPath("$.error").value(errorMessage));
  }

  @Test
  void addCurrency_shouldArgumentNotValidExceptionWhenCurrencyIsEmpty() throws Exception {
    String emptyCurrencyJson = "{\"currency\":\"\"}";
    mockMvc
        .perform(
            post("/api/v1/currencies/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(emptyCurrencyJson))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertInstanceOf(
                    MethodArgumentNotValidException.class, result.getResolvedException()))
        .andExpect(jsonPath("$.currency").value("Currency must be 3 uppercase letters"));
  }

  @Test
  void addCurrency_shouldArgumentNotValidExceptionWhenCurrencyTooLong() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/currencies/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"currency\":\"GBPQ\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertInstanceOf(
                    MethodArgumentNotValidException.class, result.getResolvedException()))
        .andExpect(jsonPath("$.currency").value("Currency must be 3 uppercase letters"));
  }

  @Test
  void addCurrency_shouldArgumentNotValidExceptionWhenCurrencyTooShort() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/currencies/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"currency\":\"G\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertInstanceOf(
                    MethodArgumentNotValidException.class, result.getResolvedException()))
        .andExpect(jsonPath("$.currency").value("Currency must be 3 uppercase letters"));
  }

  @Test
  void addCurrency_shouldArgumentNotValidExceptionWhenCurrencyInvalidSymbols() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/currencies/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"currency\":\"!!!\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertInstanceOf(
                    MethodArgumentNotValidException.class, result.getResolvedException()))
        .andExpect(jsonPath("$.currency").value("Currency must be 3 uppercase letters"));
  }

  @Test
  void addCurrency_shouldArgumentNotValidExceptionWhenCurrencyLowerCase() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/currencies/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"currency\":\"sde\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertInstanceOf(
                    MethodArgumentNotValidException.class, result.getResolvedException()))
        .andExpect(jsonPath("$.currency").value("Currency must be 3 uppercase letters"));
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
        .perform(
            post("/api/v1/currencies/some-endpoint-that-causes-error")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"currency\":\"EUR\"}"))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("Internal server error"));
  }
}
