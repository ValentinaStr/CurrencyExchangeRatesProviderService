package com.currencyexchange.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exchange_rates")
@Data
@Builder
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

  @Column(name = "rate", nullable = false)
  private BigDecimal rate;

/*  @ManyToOne
  @JoinColumn(name = "api_log_id", nullable = false)
  private ApiLogEntity apiLog;*/
}
