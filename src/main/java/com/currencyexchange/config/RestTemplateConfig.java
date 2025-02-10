package com.currencyexchange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  /**
   * Creates a {@link RestTemplate} bean.
   *
   * @return a new {@link RestTemplate} instance.
   */
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
