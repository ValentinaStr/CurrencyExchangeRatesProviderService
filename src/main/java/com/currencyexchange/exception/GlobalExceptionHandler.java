package com.currencyexchange.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles validation errors in method arguments and returns field-specific error messages.
   *
   * @param ex the exception
   * @return a map of field names and error messages
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    return errors;
  }

  /**
   * Handles RateNotFoundInCacheException and returns an error message.
   *
   * @param ex the exception
   * @return an error message
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(RateNotFoundInCacheException.class)
  public String handleRateNotFoundInCacheException(RateNotFoundInCacheException ex) {
    return ex.getMessage();
  }

  /**
   * Handles validation errors in handler methods and returns a general error message.
   *
   * @param ex the exception
   * @return a map with error messages
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HandlerMethodValidationException.class)
  public Map<String, String> handlerMethodValidationException(HandlerMethodValidationException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getAllErrors().forEach(error -> errors.put("error", error.getDefaultMessage()));
    return errors;
  }

  /**
   * Handles unexpected server errors and returns a general error message.
   *
   * @param ex the exception
   * @return a general error message
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public String handleUnexpectedException(Exception ex) {
    return "Internal server error";
  }
}
