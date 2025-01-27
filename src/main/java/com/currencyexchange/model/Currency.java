package com.currencyexchange.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "currencies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

  @Id
  @Column(nullable = false, unique = true, length = 3)
  @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be exactly 3 uppercase letters")
  private String currency;
}
