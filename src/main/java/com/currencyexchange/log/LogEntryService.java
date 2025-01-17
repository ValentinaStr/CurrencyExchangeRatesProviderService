package com.currencyexchange.log;

import com.currencyexchange.currency.model.Currency;
import com.currencyexchange.log.model.LogEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogEntryService {

  private final LogEntryRepository logEntryRepository;

  public void logCurrencies(Collection<?> result) {
    LogEntry logEntry = new LogEntry();
    logEntry.setId(null);
    logEntry.setTimestamp(LocalDateTime.now());
    logEntry.setUrl("/api/v1/currencies");

    String currencies = result.stream()
        .map(currency -> ((Currency) currency).getCurrency())
        .collect(Collectors.joining(", "));

    logEntry.setResponse(currencies);

    logEntryRepository.save(logEntry);
  }


}
