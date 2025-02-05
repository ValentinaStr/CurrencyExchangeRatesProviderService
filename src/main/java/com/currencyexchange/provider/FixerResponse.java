package com.currencyexchange.provider;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FixerResponse extends Response {
  private String base;
  private String date;

  @JsonProperty("rates")
  private Map<String, Double> rates;

  @Override
  public String getDescription() {
    return "FixerResponse{"
        + "base='"
        + base
        + '\''
        + ", rates="
        + (rates != null ? rates.toString() : "null")
        + '}';
  }
}
