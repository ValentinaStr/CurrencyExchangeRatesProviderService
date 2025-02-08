package com.currencyexchange.config;

import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
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

  @Bean
  public RestTemplate secureRestTemplate(RestTemplateBuilder builder, SslBundles sslBundles) {
    SslBundle sslBundle = sslBundles.getBundle("mybundle");
    return builder.sslBundle(sslBundle).build();
  }
}
