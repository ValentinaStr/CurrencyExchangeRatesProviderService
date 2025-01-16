package com.currencyexchange.log;

import com.currencyexchange.log.model.LogEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
