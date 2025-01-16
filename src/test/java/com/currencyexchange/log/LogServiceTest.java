package com.currencyexchange.log;

import com.currencyexchange.log.model.LogEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {

  @Mock
  private LogRepository logRepository;

  @InjectMocks
  private LogService logService;

  @Test
  void testLogResponse_shouldSaveLogEntry() {
    // Arrange
    String url = "/api/v1/currencies";
    String response = "[USD, EUR]";
    LocalDateTime now = LocalDateTime.now();

    // Act
    logService.logResponse(url, response);

    // Assert
    LogEntry expectedLogEntry = new LogEntry(null, now, url, response);
    // Поскольку время может быть разным, сравниваем только без учета времени
    verify(logRepository, times(1)).save(argThat(logEntry ->
        logEntry.getUrl().equals(url) &&
            logEntry.getResponse().equals(response)
    ));
  }

  @Test
  void testLogResponse_shouldSaveLogEntryWithNonNullTimestamp() {
    // Arrange
    String url = "/api/v1/currencies";
    String response = "[USD, EUR]";

    // Act
    logService.logResponse(url, response);

    // Assert
    verify(logRepository, times(1)).save(argThat(logEntry ->
        logEntry.getTimestamp() != null &&
            logEntry.getUrl().equals(url) &&
            logEntry.getResponse().equals(response)
    ));
  }

  @Test
  void testLogResponse_shouldSaveLogEntryWithCorrectData() {
    // Arrange
    String url = "/api/v1/currencies";
    String response = "[USD, EUR]";

    // Act
    logService.logResponse(url, response);

    // Assert
    verify(logRepository, times(1)).save(argThat(logEntry ->
        logEntry.getUrl().equals(url) &&
            logEntry.getResponse().equals(response)
    ));
  }
}
