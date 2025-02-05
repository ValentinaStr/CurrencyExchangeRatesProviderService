package com.currencyexchange.provider;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Response {

  /**
   * Returns the description of the response.
   *
   * @return the description as a string.
   */
  public abstract String getDescription();
}
