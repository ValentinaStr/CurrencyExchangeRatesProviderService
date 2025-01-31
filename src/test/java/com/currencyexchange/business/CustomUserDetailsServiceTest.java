package com.currencyexchange.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.currencyexchange.model.RoleEntity;
import com.currencyexchange.model.UserEntity;
import com.currencyexchange.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CustomUserDetailsService customUserDetailsService;

  private UserEntity user;

  @BeforeEach
  void setUp() {
    RoleEntity role = new RoleEntity(1L, "ROLE_USER");
    user = new UserEntity(1L, "testUser", "password123", role);
  }

  @Test
  void loadUserByUsername_shouldReturnUserDetailsUserExists() {
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

    UserDetails userDetails = customUserDetailsService.loadUserByUsername("testUser");

    assertEquals("testUser", userDetails.getUsername());
    assertEquals("password123", userDetails.getPassword());
    assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());

    verify(userRepository, times(1)).findByUsername("testUser");
  }

  @Test
  void loadUserByUsername_shouldThrowExceptionWhenUserNotFound() {
    when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

    assertThrows(
        UsernameNotFoundException.class,
        () -> customUserDetailsService.loadUserByUsername("unknownUser"));

    verify(userRepository, times(1)).findByUsername("unknownUser");
  }
}
