package com.currencyexchange.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Represents a single validation error.
 *
 * @param field the field that failed validation, or null for non-field errors
 * @param message human-readable error message
 */
@JsonInclude(Include.NON_NULL)
public record ValidationError(String field, String message) {

  public static ValidationError of(String message) {
    return new ValidationError(null, message);
  }
}
