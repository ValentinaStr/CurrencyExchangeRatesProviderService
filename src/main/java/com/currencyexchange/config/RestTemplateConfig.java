package com.currencyexchange.config;

import javax.net.ssl.SSLContext;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  //  @Value("${openexchangerates.api.truststore}")
  //  private Resource trustStore;
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
