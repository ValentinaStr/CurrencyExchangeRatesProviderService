package com.currencyexchange.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test")
public abstract class TestContainerConfig {

  @Container
  @ServiceConnection
  static final PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>("postgres:15-alpine");
}
