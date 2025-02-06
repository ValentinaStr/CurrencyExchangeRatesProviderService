package com.currencyexchange.provider;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FixerResponse extends Response {

  private String base;
  private Long timestamp;
  private String date;

  @JsonIgnore
  private LocalDateTime dateTime;

  private Map<String, BigDecimal> rates;

  public LocalDateTime getDateTime() {
    return Instant.ofEpochSecond(timestamp).atOffset(ZoneOffset.UTC).toLocalDateTime();
  }
}
