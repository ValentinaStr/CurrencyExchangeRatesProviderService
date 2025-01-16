package com.currencyexchange.log;

import com.currencyexchange.log.model.LogEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static wiremock.net.javacrumbs.jsonunit.core.internal.JsonUtils.convertToJson;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {

  private final LogRepository logRepository;

  public void logResponse(String url, String logResponse) {

    LogEntry logEntry = new LogEntry(
        null,
        LocalDateTime.now(),
        url,
        logResponse
    );

    logRepository.save(logEntry);
  }
}
