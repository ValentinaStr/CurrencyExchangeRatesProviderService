package com.currencyexchange.business;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import com.currencyexchange.model.ApiLogEntity;
import com.currencyexchange.provider.Response;
import com.currencyexchange.repository.ApiLogRepository;
import java.time.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ApiLogServiceTest {

  @Mock
  private ApiLogRepository apiLogRepository;

  @Mock
  private Response response;

  @InjectMocks
  private ApiLogService apiLogService;




  @Test
  void saveApiLog_shouldSaveLog() {
    String url = "http://localhost/api";
    String responseDescription = "Success Response";
    LocalDateTime dateTime = LocalDateTime.of(2025, Month.FEBRUARY, 6, 14, 30, 0, 0);

    when(response.getDescription()).thenReturn(responseDescription);
    when(response.getDateTime()).thenReturn(dateTime);
    ApiLogEntity apiLogEntity =
        ApiLogEntity.builder()
            .timestamp(dateTime)
            .url(url)
            .response(responseDescription)
            .build();
    when(apiLogRepository.save(apiLogEntity)).thenReturn(apiLogEntity);

    apiLogService.saveApiLog(url, response);

    verify(apiLogRepository).save(apiLogEntity);
  }
}

