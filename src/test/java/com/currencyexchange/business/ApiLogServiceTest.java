package com.currencyexchange.business;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.entity.ApiLogEntity;
import com.currencyexchange.model.RatesModel;
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

  @Mock
  private ApiLogRepository apiLogRepository;

  @Mock
  private RatesModel response;

  @InjectMocks
  private ApiLogService apiLogService;

  @Test
  void saveApiLog_shouldSaveLog() {
    String url = "http://localhost/api";
    RatesModel response = RatesModel.builder()
        .base("EUR")
        .timestamp(1707302400L)
        .rates(Map.of(
            "GBP", new BigDecimal("0.79"),
            "JPY", new BigDecimal("148.25")
        ))
        .build();

    ApiLogEntity apiLog =
        ApiLogEntity.builder()
            .timestamp(
                Instant.ofEpochSecond(response.timestamp())
                    .atOffset(ZoneOffset.UTC)
                    .toLocalDateTime())
            .url(url)
            .response(response.toString())
            .build();
    when(apiLogRepository.save(apiLog)).thenReturn(apiLog);

    apiLogService.logRequest(url, response);

    verify(apiLogRepository).save(apiLog);
  }
}
