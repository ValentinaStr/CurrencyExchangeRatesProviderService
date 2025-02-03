package com.currencyexchange.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exchange_rates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "base_currency", nullable = false)
  private String baseCurrency;

  @Column(name = "target_currency", nullable = false)
  private String targetCurrency;

  @Column(name = "rate", precision = 10, scale = 6, nullable = false)
  private BigDecimal rate;
}
