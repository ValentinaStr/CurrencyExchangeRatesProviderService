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

  @Mock private ApiLogRepository apiLogRepository;

  @Mock private Response response;

  @InjectMocks private ApiLogService apiLogService;

  private LocalDateTime fixedDateTime;

  @BeforeEach
  void setUp() {
    fixedDateTime = LocalDateTime.of(2014, 11, 4, 12, 30, 0);
    MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class);
    mockedStatic.when(LocalDateTime::now).thenReturn(fixedDateTime);
  }

  @Test
  void saveApiLog_shouldSaveLog() {
    String url = "http://localhost/api";
    String responseDescription = "Success Response";
    when(response.getDescription()).thenReturn(responseDescription);
    ApiLogEntity apiLogEntity =
        ApiLogEntity.builder()
            .timestamp(fixedDateTime)
            .url(url)
            .response(responseDescription)
            .build();
    when(apiLogRepository.save(apiLogEntity)).thenReturn(apiLogEntity);

    apiLogService.saveApiLog(url, response);

    verify(apiLogRepository).save(apiLogEntity);
  }
}
