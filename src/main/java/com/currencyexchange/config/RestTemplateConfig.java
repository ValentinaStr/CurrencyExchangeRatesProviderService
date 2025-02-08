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

  /**
   * Creates a {@link RestTemplate} configured with SSL from the "mybundle" {@link SslBundle}.
   *
   * @param builder the {@link RestTemplateBuilder} for building the {@link RestTemplate}.
   * @param sslBundles the {@link SslBundles} providing SSL configurations.
   * @return a secure {@link RestTemplate}.
   */
  @Bean
  public RestTemplate secureRestTemplate(RestTemplateBuilder builder, SslBundles sslBundles) {
    SslBundle sslBundle = sslBundles.getBundle("mybundle");
    return builder.sslBundle(sslBundle).build();
  }
}
