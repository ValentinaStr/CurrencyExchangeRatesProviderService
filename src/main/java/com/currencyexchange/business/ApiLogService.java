package com.currencyexchange.business;

import com.currencyexchange.model.ApiLogEntity;
import com.currencyexchange.repository.ApiLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiLogService {

  private final ApiLogRepository apiLogRepository;

  @Autowired
  public ApiLogService(ApiLogRepository apiLogRepository) {
    this.apiLogRepository = apiLogRepository;
  }

  public void saveApiLog(String url, Object response) throws IOException {

    ApiLogEntity apiLog =
        ApiLogEntity.builder()
            .timestamp(LocalDateTime.now())
            .url(url)
            .response(new ObjectMapper().writeValueAsString(response))
            .build();

    apiLogRepository.save(apiLog);
  }
}
