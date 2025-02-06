package com.currencyexchange.provider;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Response {
  String base;
  LocalDateTime dateTime;
  Map<String, BigDecimal> rates;
}
