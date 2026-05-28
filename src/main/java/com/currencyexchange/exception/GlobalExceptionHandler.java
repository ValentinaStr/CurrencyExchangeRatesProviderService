package com.currencyexchange.exception;

import com.currencyexchange.model.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidationException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    log.warn("Validation failed for request {}: {}", request.getRequestURI(), ex.getMessage());
    ProblemDetail pd = ex.getBody();

    List<ValidationError> errors =
        ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(e -> new ValidationError(e.getField(), e.getDefaultMessage()))
            .toList();

    pd.setProperty("errors", errors);
    return enrich(pd, request);
  }

  @ExceptionHandler(HandlerMethodValidationException.class)
  public ProblemDetail handleMethodValidationException(
      HandlerMethodValidationException ex, HttpServletRequest request) {

    log.warn("Method validation failed for request {}: {}", request.getRequestURI(), ex.getMessage());
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setTitle("Validation failed");

    List<ValidationError> errors =
        ex.getAllErrors().stream().map(e -> ValidationError.of(e.getDefaultMessage())).toList();

    pd.setProperty("errors", errors);
    return enrich(pd, request);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ProblemDetail handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex, HttpServletRequest request) {

    log.warn("Unreadable request body for request {}: {}", request.getRequestURI(), ex.getMessage());
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setTitle("Bad Request");
    return enrich(pd, request);
  }

  @ExceptionHandler(RateNotFoundInCacheException.class)
  public ProblemDetail handleRateNotFound(
      RateNotFoundInCacheException ex, HttpServletRequest request) {

    log.warn("Rate not found for request {}: {}", request.getRequestURI(), ex.getMessage());
    ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    pd.setTitle("Rate not found");
    return enrich(pd, request);
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleUnexpectedException(Exception ex, HttpServletRequest request) {

    log.error("Unexpected error occurred", ex);

    ProblemDetail pd =
        ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    pd.setTitle("Internal server error");
    return enrich(pd, request);
  }

  private ProblemDetail enrich(ProblemDetail pd, HttpServletRequest request) {
    pd.setInstance(URI.create(request.getRequestURI()));
    pd.setProperty("traceId", UUID.randomUUID().toString());
    return pd;
  }
}
