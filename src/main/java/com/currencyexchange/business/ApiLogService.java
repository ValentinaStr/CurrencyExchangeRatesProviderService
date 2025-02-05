package com.currencyexchange.business;

import com.currencyexchange.model.ApiLogEntity;
import com.currencyexchange.provider.Response;
import com.currencyexchange.repository.ApiLogRepository;
import java.time.LocalDateTime;
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
  public void saveApiLog(String url, Response response) {
    ApiLogEntity apiLog =
        ApiLogEntity.builder()
            .timestamp(LocalDateTime.now())
            .url(url)
            .response(response.getDescription())
            .build();

    apiLogRepository.save(apiLog);
    log.info("Successfully saved API log for URL: {}", url);
  }
}
