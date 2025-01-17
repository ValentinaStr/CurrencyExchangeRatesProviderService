package com.currencyexchange.log.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "api_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogEntry {

  @Id
  @GeneratedValue
  private UUID id = UUID.randomUUID();

  @Column(name = "timestamp")
  private LocalDateTime timestamp;

  @Column(name = "url")
  private String url;

  @Column(name = "response", columnDefinition = "TEXT")
  private String response;
}
