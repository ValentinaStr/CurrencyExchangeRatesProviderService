package com.currencyexchange.exception;

public class ExchangeRateClientUnavailableException extends RuntimeException {

  /**
   * Constructs a new exception with the specified detail message.
   *
   * @param message the detail message explaining the reason for the failure
   */
  public ExchangeRateClientUnavailableException(String message) {
    super(message);
  }
}
