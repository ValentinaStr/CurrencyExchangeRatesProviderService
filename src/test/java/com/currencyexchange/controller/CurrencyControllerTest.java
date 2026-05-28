package com.currencyexchange.controller;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.currencyexchange.business.CurrencyService;
import com.currencyexchange.entity.CurrencyEntity;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {

  @Mock
  private CurrencyService currencyService;

  @InjectMocks
  private CurrencyController currencyController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(currencyController).build();
  }

  @Test
  void getAllCurrencies_shouldReturnCurrencies() throws Exception {
    when(currencyService.getAllCurrencies()).thenReturn(Set.of("USD", "EUR"));

    mockMvc
        .perform(get("/api/v1/currencies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currencies").isArray())
        .andExpect(jsonPath("$.currencies", hasItems("USD", "EUR")));

    verify(currencyService).getAllCurrencies();
  }

  @Test
  void getAllCurrencies_shouldReturnEmptyListWhenNoCurrencies() throws Exception {
    when(currencyService.getAllCurrencies()).thenReturn(Set.of());

    mockMvc
        .perform(get("/api/v1/currencies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currencies").isArray())
        .andExpect(jsonPath("$.currencies").isEmpty());

    verify(currencyService).getAllCurrencies();
  }

  @Test
  void addCurrency_shouldSaveCurrency() throws Exception {
    CurrencyEntity currencyValid = new CurrencyEntity("GBP");
    doNothing().when(currencyService).addCurrency(currencyValid);
    String currencyJson =
        """
        {
          "currency": "GBP"
        }
        """;

    mockMvc
        .perform(
            post("/api/v1/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(currencyJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("Currency processed: GBP"));

    verify(currencyService).addCurrency(currencyValid);
  }
}
