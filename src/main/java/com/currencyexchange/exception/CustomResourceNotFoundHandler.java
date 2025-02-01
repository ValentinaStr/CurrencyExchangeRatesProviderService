package com.currencyexchange.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomResourceNotFoundHandler implements AccessDeniedHandler {

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException {
    response.setStatus(HttpStatus.NOT_FOUND.value());
    response.setContentType("application/json");

    String jsonResponse =
        """
                {
                    "error": "Resource not found",
                    "message": "Resource not found"
                }
            """;

    response.getWriter().write(jsonResponse);
  }
}
