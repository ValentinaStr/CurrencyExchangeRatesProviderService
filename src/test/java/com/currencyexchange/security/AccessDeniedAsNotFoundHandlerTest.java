package com.currencyexchange.security;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.security.AccessDeniedAsNotFoundHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class AccessDeniedAsNotFoundHandlerTest {

  @Mock
  private ObjectMapper objectMapper;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @InjectMocks
  private AccessDeniedAsNotFoundHandler handler;

  @Test
  void handle_shouldReturn404() throws Exception {
    when(request.getRequestURI()).thenReturn("/api/v1/currencies");
    when(response.getWriter()).thenReturn(mock(PrintWriter.class));

    handler.handle(request, response, new AccessDeniedException("Denied"));

    verify(response).setStatus(404);
    verify(response).setContentType("application/problem+json");
  }
}
