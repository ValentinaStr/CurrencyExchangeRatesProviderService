package com.currencyexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

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
