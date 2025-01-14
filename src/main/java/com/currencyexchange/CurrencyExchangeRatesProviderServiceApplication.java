package com.currencyexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class of the Currency Converter application.
 * This class contains the entry point for the application.
 */
@SpringBootApplication
public class CurrencyExchangeRatesProviderServiceApplication {
  /**
   * The main method that runs the application.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(CurrencyExchangeRatesProviderServiceApplication.class, args);
  }
}
