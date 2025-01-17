package com.currencyexchange.currency;

import com.currencyexchange.currency.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}
