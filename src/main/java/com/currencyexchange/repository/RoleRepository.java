package com.currencyexchange.repository;

import com.currencyexchange.model.RoleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
  /**
   * Finds a role by name.
   *
   * @param name role name
   * @return Optional containing the role if found
   */
  Optional<RoleEntity> findByName(String name);
}
