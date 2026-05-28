package com.currencyexchange.business;

import com.currencyexchange.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByUsername(username)
        .map(user -> {
          log.debug("User loaded successfully: {}", username);
          return User.builder()
              .username(user.getUsername())
              .password(user.getPassword())
              .authorities(new SimpleGrantedAuthority(user.getRole().getName()))
              .build();
        })
        .orElseThrow(() -> {
          log.warn("User not found: {}", username);
          return new UsernameNotFoundException("User not found: " + username);
        });
  }
}
