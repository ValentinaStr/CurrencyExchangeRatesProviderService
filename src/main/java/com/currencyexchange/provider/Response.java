package com.currencyexchange.provider;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Response {

  String base;
  LocalDateTime dateTime;
  Map<String, Double> rates;

  /**
   * Returns the description of the response.
   *
   * @return the description as a string.
   */
  public abstract String getDescription();
}
