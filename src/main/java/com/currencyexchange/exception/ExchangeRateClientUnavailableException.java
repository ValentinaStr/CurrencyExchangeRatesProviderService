package com.currencyexchange.exception;

public class ExchangeRateClientUnavailableException extends RuntimeException {

  /**
   * Constructs a new exception with the specified detail message and cause.
   *
   * @param message the detail message explaining the reason for the failure
   * @param cause the underlying exception that caused this failure
   */
  public ExchangeRateClientUnavailableException(String message, Throwable cause) {
    super(message, cause);
  }
}
