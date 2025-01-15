package com.currencyexchange.currency;

import com.currencyexchange.currency.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Currency} entities.
 * Provides basic CRUD operations on the {@link Currency} entity.
 * This interface extends {@link JpaRepository}, which provides built-in methods for
 * interacting with the database, such as saving, deleting, and finding entities.
 */
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}
