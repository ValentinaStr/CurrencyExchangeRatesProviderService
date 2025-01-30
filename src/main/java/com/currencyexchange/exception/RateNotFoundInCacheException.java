package com.currencyexchange.exception;

public class RateNotFoundInCacheException extends RuntimeException {
  /**
   * Constructs a new {@code ExchangeRateNotFoundInCacheException} with the specified detail
   * message.
   *
   * @param message The detail message explaining the reason for the exception.
   */
  public RateNotFoundInCacheException(String message) {
    super(message);
  }
}
