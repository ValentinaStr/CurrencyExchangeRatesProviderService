package com.currencyexchange.repository;

import com.currencyexchange.model.ApiLogEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiLogRepository extends JpaRepository<ApiLogEntity, UUID> {}
