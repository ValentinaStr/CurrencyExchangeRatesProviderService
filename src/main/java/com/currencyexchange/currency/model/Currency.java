package com.currencyexchange.currency.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Entity representing a currency.
 * This class holds the information about the currency,
 * including a unique identifier and the currency code.
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "currencies")
public class Currency {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String currency;
}
