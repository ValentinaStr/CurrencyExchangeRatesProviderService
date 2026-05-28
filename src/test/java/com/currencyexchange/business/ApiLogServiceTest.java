package com.currencyexchange.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.currencyexchange.entity.ApiLogEntity;
import com.currencyexchange.model.RatesModel;
import com.currencyexchange.repository.ApiLogRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ApiLogServiceTest {

  @Mock
  private ApiLogRepository apiLogRepository;

  @InjectMocks
  private ApiLogService apiLogService;

  @Test
  void logRequest_shouldSaveToRepository() {
    String url = "http://localhost/api";
    RatesModel rates = RatesModel.builder()
        .base("EUR")
        .timestamp(1707302400L)
        .rates(Map.of(
            "GBP", new BigDecimal("0.79"),
            "JPY", new BigDecimal("148.25")
        ))
        .build();

    ApiLogEntity expected = ApiLogEntity.builder()
        .timestamp(LocalDateTime.ofInstant(Instant.ofEpochSecond(rates.timestamp()), ZoneOffset.UTC))
        .url(url)
        .response(rates.toString())
        .build();

    apiLogService.logRequest(url, rates);

    verify(apiLogRepository).save(expected);
  }

  @Test
  void logRequest_shouldDoNothingWhenRatesIsNull() {
    apiLogService.logRequest("http://localhost/api", null);

    verifyNoInteractions(apiLogRepository);
  }

  @Test
  void logRequest_shouldSaveWhenUrlIsNull() {
    RatesModel rates = RatesModel.builder()
        .base("EUR")
        .timestamp(1707302400L)
        .rates(Map.of("GBP", new BigDecimal("0.79")))
        .build();

    apiLogService.logRequest(null, rates);

    ArgumentCaptor<ApiLogEntity> captor = ArgumentCaptor.forClass(ApiLogEntity.class);
    verify(apiLogRepository).save(captor.capture());
    assertEquals("unknown", captor.getValue().getUrl());
  }
}
