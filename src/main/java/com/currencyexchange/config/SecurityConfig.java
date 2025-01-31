package com.currencyexchange.config;

import static org.springframework.security.config.Customizer.withDefaults;

import jakarta.servlet.http.HttpServletResponse;
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
                    .requestMatchers(HttpMethod.POST, "/api/v1/currencies/")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated())
        .httpBasic(withDefaults())
        .exceptionHandling(
            exceptions ->
                exceptions.accessDeniedHandler(
                    (request, response, accessDeniedException) ->
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found")))
        .build();
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
