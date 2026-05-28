package com.currencyexchange.business;

import com.currencyexchange.entity.ApiLogEntity;
import com.currencyexchange.model.RatesModel;
import com.currencyexchange.repository.ApiLogRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiLogService {

  private final ApiLogRepository apiLogRepository;

  /**
   * Persists an external API request log entry to the database.
   *
   * @param url the external API request URL
   * @param rates the exchange rates model returned by the external API
   */
  public void logRequest(String url, RatesModel rates) {
    if (rates == null) {
      log.warn("Skipping API log: rates is null for URL: {}", url);
      return;
    }
    String effectiveUrl = url != null ? url : "unknown";
    ApiLogEntity apiLog =
        ApiLogEntity.builder()
            .timestamp(
                LocalDateTime.ofInstant(Instant.ofEpochSecond(rates.timestamp()), ZoneOffset.UTC))
            .url(effectiveUrl)
            .response(rates.toString())
            .build();

    apiLogRepository.save(apiLog);
    log.debug("Successfully saved API log for URL: {}", effectiveUrl);
  }
}
