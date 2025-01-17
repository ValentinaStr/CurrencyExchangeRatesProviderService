package com.currencyexchange.currency.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "currencies")
public class Currency {

  @Id
  @GeneratedValue
  private UUID id = UUID.randomUUID();

  @Column(nullable = false, unique = true)
  private String currency;
}
