package com.currencyexchange.business;

import com.currencyexchange.entity.ApiLogEntity;
import com.currencyexchange.model.Rates;
import com.currencyexchange.repository.ApiLogRepository;
import java.time.Instant;
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
   * Saves the request URL and rates to the database.
   *
   * @param url the request URL.
   * @param rates the rates object to be saved as a string.
   */
  public void logRequest(String url, Rates rates) {
    ApiLogEntity apiLog =
        ApiLogEntity.builder()
            .timestamp(
                Instant.ofEpochSecond(rates.timestamp()).atOffset(ZoneOffset.UTC).toLocalDateTime())
            .url(url)
            .response(rates.toString())
            .build();

    apiLogRepository.save(apiLog);
    log.info("Successfully saved API log for URL: {}", url);
  }
}
