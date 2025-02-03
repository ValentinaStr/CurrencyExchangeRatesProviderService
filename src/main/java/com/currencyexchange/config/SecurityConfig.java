package com.currencyexchange.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.currencyexchange.exception.CustomAuthenticationRequiredHandler;
import com.currencyexchange.exception.CustomResourceNotFoundHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * Configures security settings, including authorization and exception handling.
   *
   * @param http HttpSecurity object to configure security settings
   * @return SecurityFilterChain with defined security rules
   * @throws Exception if an error occurs during configuration
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authz ->
                authz
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/currencies/")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated())
        .httpBasic(withDefaults())
        .exceptionHandling(
            exceptions ->
                exceptions
                    .accessDeniedHandler((new CustomResourceNotFoundHandler()))
                    .authenticationEntryPoint((new CustomAuthenticationRequiredHandler())))
        .build();
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
