package com.currencyexchange.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "api_logs")
public class ApiLogEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false)
  private LocalDateTime timestamp;

  @Column(nullable = false)
  private String url;

  @Column(nullable = false)
  private String response;

/*  @OneToMany(mappedBy = "apiLog")
  private List<ExchangeRateEntity> exchangeRates = new ArrayList<>();*/
}
