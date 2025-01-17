package com.currencyexchange.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Collection;

@Aspect
@Component
public class DatabaseLoggingAspect {
  @Autowired
  private LogEntryService  logEntryService;

  @Pointcut("execution(public * com.currencyexchange.currency.CurrencyRepository.findAll(..))")
  private void findAll() {
  }

  @AfterReturning(value = "findAll()", returning = "result")
  public void logAfterFindAllOperation(JoinPoint joinPoint, Object result) {
    logEntryService.logCurrencies((Collection<?>) result);
  }
}
