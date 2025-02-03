package com.currencyexchange.repository;

import com.currencyexchange.model.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  /**
   * Finds a user by username.
   *
   * @param username the username
   * @return Optional containing the user if found
   */
  Optional<UserEntity> findByUsername(String username);
}
