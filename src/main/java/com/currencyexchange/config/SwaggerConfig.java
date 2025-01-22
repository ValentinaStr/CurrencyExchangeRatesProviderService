package com.currencyexchange.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  /**
   * Configures the OpenAPI documentation for the Currency Exchange API.
   * Provides general information about the API and defines the version, title, and description.
   */
  @Bean
  public OpenAPI openApiConfig() {
    return new OpenAPI()
        .info(new Info()
            .title("Currency Exchange API")
            .version("1.0")
            .description("API documentation for Currency Exchange application"));
  }

  /**
   * Configures a grouped OpenAPI specification for the public API.
   * This defines the base path for the API documentation as `/api/**`.
   *
   * @return a {@link GroupedOpenApi} for the public API documentation.
   */
  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("v1")
        .pathsToMatch("/api/**")
        .build();
  }
}
