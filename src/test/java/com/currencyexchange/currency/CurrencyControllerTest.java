package com.currencyexchange.currency;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the {@link CurrencyController}.
 * This class contains unit tests for the {@link CurrencyController} methods,
 * ensuring that the controller correctly
 * handles requests to retrieve the list of currencies.
 */
@ExtendWith(MockitoExtension.class)
public class CurrencyControllerTest {

  private MockMvc mockMvc;
  private List<String> mockCurrencies;

  @Mock
  private CurrencyService currencyService;

  @InjectMocks
  private CurrencyController currencyController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(currencyController).build();
    mockCurrencies = Arrays.asList("USD", "EUR");
  }

  @Test
  void testGetAllCurrencies() throws Exception {
    when(currencyService.getAllCurrencies()).thenReturn(mockCurrencies);
    mockMvc.perform(get("/api/v1/currencies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0]").value("USD"))
        .andExpect(jsonPath("$[1]").value("EUR"));
    verify(currencyService, times(1)).getAllCurrencies();
  }

  @Test
  void testGetAllCurrencies_empty() throws Exception {
    when(currencyService.getAllCurrencies()).thenReturn(List.of());
    mockMvc.perform(get("/api/v1/currencies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
    verify(currencyService, times(1)).getAllCurrencies();
  }
}
