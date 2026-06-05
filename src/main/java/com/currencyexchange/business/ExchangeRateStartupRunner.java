package com.currencyexchange.business;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class ExchangeRateStartupRunner implements ApplicationRunner {

  private final ExchangeRateUpdateService exchangeRateUpdateService;

  @Override
  public void run(ApplicationArguments args) {
    exchangeRateUpdateService.refreshRates();
  }

  @Scheduled(fixedRate = 3600000)
  public void scheduledRefresh() {
    exchangeRateUpdateService.refreshRates();
  }
}
