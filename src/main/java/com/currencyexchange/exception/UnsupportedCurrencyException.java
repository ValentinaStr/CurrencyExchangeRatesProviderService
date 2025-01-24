package com.currencyexchange.exception;

public class UnsupportedCurrencyException extends Exception  {

  /**
   * Constructs a new UnsupportedCurrencyException with the specified detail message.
   *
   * @param message The detail message,
   *                which is saved for later retrieval by the {@link Throwable#getMessage()} method.
   */
  public UnsupportedCurrencyException(String message) {
    super(message);
  }
}
