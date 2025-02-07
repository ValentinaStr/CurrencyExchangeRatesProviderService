package com.currencyexchange.dto;

import java.math.BigDecimal;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExchangeRateResponseDto {
  private boolean success;
  private long timestamp;
  private String base;
  private String date;
  private Map<String, BigDecimal> rates;
}
