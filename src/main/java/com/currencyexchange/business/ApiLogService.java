package com.currencyexchange.business;

import com.currencyexchange.dto.ExchangeRateResponseDto;
import com.currencyexchange.model.ApiLogEntity;
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
   * Saves the request URL and response to the database.
   *
   * @param url the request URL.
   * @param response the response object to be saved as a string.
   */
  public void logRequest(String url, ExchangeRateResponseDto response) {
    ApiLogEntity apiLog =
        ApiLogEntity.builder()
            .timestamp(
                Instant.ofEpochSecond(response.timestamp())
                    .atOffset(ZoneOffset.UTC)
                    .toLocalDateTime())
            .url(url)
            .response(response.toString())
            .build();

    apiLogRepository.save(apiLog);
    log.info("Successfully saved API log for URL: {}", url);
  }
}
