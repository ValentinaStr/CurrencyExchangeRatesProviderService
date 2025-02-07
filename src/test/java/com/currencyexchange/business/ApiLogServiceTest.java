package com.currencyexchange.business;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.dto.ExchangeRateResponseDto;
import com.currencyexchange.model.ApiLogEntity;
import com.currencyexchange.repository.ApiLogRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ApiLogServiceTest {

  @Mock private ApiLogRepository apiLogRepository;

  @Mock private ExchangeRateResponseDto response;

  @InjectMocks private ApiLogService apiLogService;

  @Test
  void saveApiLog_shouldSaveLog() {
    String url = "http://localhost/api";
    ExchangeRateResponseDto response =
        ExchangeRateResponseDto.builder()
            .base("EUR")
            .timestamp(1707302400L)
            .date("2025-02-07")
            .rates(
                Map.of(
                    "GBP", new BigDecimal("0.79"),
                    "JPY", new BigDecimal("148.25")))
            .build();
    ApiLogEntity apiLog =
        ApiLogEntity.builder()
            .timestamp(
                Instant.ofEpochSecond(response.getTimestamp())
                    .atOffset(ZoneOffset.UTC)
                    .toLocalDateTime())
            .url(url)
            .response(response.getBase() + response.getRates().toString())
            .build();
    when(apiLogRepository.save(apiLog)).thenReturn(apiLog);

    apiLogService.logRequest(url, response);

    verify(apiLogRepository).save(apiLog);
  }
}
