package com.currencyexchange.repository;

import com.currencyexchange.model.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<CurrencyCode, String> {
}
