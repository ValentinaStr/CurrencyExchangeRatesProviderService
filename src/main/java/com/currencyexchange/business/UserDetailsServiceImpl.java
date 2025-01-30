package com.currencyexchange.business;

import com.currencyexchange.model.UserEntity;
import com.currencyexchange.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        Collections.singleton(new SimpleGrantedAuthority(user.getRole().getName())));
  }
}
