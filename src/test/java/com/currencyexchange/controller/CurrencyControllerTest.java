package com.currencyexchange.currency;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.currencyexchange.business.CurrencyService;
import com.currencyexchange.controller.CurrencyController;
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
    mockCurrencies = Set.of("USD", "EUR");;
  }

  @Test
  void testGetAllCurrencies() throws Exception {
    when(currencyService.getAllCurrencies()).thenReturn(mockCurrencies);
    mockMvc.perform(get("/api/v1/currencies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasItems("USD", "EUR")));
    verify(currencyService, times(1)).getAllCurrencies();
  }

  @Test
  void testGetAllCurrencies_empty() throws Exception {
    when(currencyService.getAllCurrencies()).thenReturn(Set.of());
    mockMvc.perform(get("/api/v1/currencies"))
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
}
