package com.currencyexchange.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "currencies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyEntity {

  @Id
  @Column(nullable = false, unique = true, length = 3)
  private String currency;
}
