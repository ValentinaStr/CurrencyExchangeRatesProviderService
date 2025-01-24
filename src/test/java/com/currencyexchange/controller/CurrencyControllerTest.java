package com.currencyexchange.controller;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.currencyexchange.business.CurrencyService;
import com.currencyexchange.exception.UnsupportedCurrencyException;
import java.util.Set;
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
    String newCurrency = "USD";
    when(currencyService.addCurrency(newCurrency)).thenReturn(true);

    mockMvc.perform(post("/api/v1/currencies/")
            .param("currency", newCurrency))
        .andExpect(status().isCreated())
        .andExpect(content().string("Currency added: " + newCurrency));
    verify(currencyService, times(1)).addCurrency(newCurrency);
  }

  @Test
  void testAddCurrency_errorInvalidCurrency() throws Exception {
    String invalidCurrency = "XYZ";
    doThrow(new UnsupportedCurrencyException("Invalid currency: " + invalidCurrency))
        .when(currencyService).addCurrency(invalidCurrency);

    mockMvc.perform(post("/api/v1/currencies/")
            .param("currency", invalidCurrency))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid currency: " + invalidCurrency));
    verify(currencyService, times(1)).addCurrency(invalidCurrency);
  }

  @Test
  void testAddCurrency_alreadyExists() throws Exception {
    String existingCurrency = "USD";
    when(currencyService.addCurrency(existingCurrency)).thenReturn(false);

    mockMvc.perform(post("/api/v1/currencies/")
            .param("currency", existingCurrency))
        .andExpect(status().isOk())
        .andExpect(content().string("Currency already exists: " + existingCurrency));
    verify(currencyService, times(1)).addCurrency(existingCurrency);
  }
}
