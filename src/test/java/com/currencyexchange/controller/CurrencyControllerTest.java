package com.currencyexchange.controller;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.currencyexchange.business.CurrencyService;
import java.util.Set;
import com.currencyexchange.model.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class CurrencyControllerTest {

  @Mock
  private CurrencyService currencyService;

  @InjectMocks
  private CurrencyController currencyController;

  private MockMvc mockMvc;
  private Set<String> mockCurrencies;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(currencyController).build();
    mockCurrencies = Set.of("USD", "EUR");
  }

  @Test
  void testGetAllCurrencies() throws Exception {
    when(currencyService.getAllCurrencies()).thenReturn(mockCurrencies);

    mockMvc.perform(get("/api/v1/currencies/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasItems("USD", "EUR")));
    verify(currencyService, times(1)).getAllCurrencies();
  }

  @Test
  void testGetAllCurrencies_empty() throws Exception {
    when(currencyService.getAllCurrencies()).thenReturn(Set.of());

    mockMvc.perform(get("/api/v1/currencies/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
    verify(currencyService, times(1)).getAllCurrencies();
  }

  @Test
  void testGetAllCurrencies_invalidUrl() throws Exception {
    mockMvc.perform(get("/api/v1/invalid-path"))
        .andExpect(status().isNotFound());
  }

  @Test
  void testAddCurrency_success() throws Exception {
    String validCurrency = "USD";
    mockMvc.perform(post("/api/v1/currencies/")
            .param("currency", validCurrency))
        .andExpect(status().isOk())
        .andExpect(content().string("Currency processed: USD"));

    verify(currencyService, times(1)).addCurrency(new Currency(validCurrency));
  }

  @Test
  void testAddCurrency_invalid() throws Exception {
    String invalidCurrency = "US";
    mockMvc.perform(post("/api/v1/currencies/")
            .param("currency", invalidCurrency))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Validation errors found"));

    verify(currencyService, times(0)).addCurrency(any());
  }

  @Test
  void testAddCurrency_emptyParam() throws Exception {
    mockMvc.perform(post("/api/v1/currencies/")
            .param("currency", ""))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Validation errors found"));

    verify(currencyService, times(0)).addCurrency(any());
  }
}
