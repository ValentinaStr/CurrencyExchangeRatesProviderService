package com.currencyexchange.enums;

import com.currencyexchange.exception.UnsupportedCurrencyException;

import java.util.EnumSet;

/**
 * Enum representing valid currency codes.
 * Provides functionality to check if a given currency code is supported.
 */
public enum ValidCurrencyEnum {
  USD("USD"),
  EUR("EUR"),
  GBP("GBP"),
  JPY("JPY"),
  PLN("PLN"),
  SEK("SEK"),
  NOK("NOK"),
  DKK("DKK"),
  BGN("BGN"),
  AUD("AUD"),
  CAD("CAD"),
  CZK("CZK"),
  CHF("CHF"),
  GEL("GEL"),
  CNY("CNY"),
  HUF("HUF"),
  TRY("TRY"),
  ILS("ILS"),
  RON("RON"),
  ISK("ISK"),
  BYN("BYN"),
  RUB("RUB"),
  UAH("UAH"),
  AED("AED"),
  EGP("EGP"),
  HKD("HKD"),
  KRW("KRW"),
  KZT("KZT"),
  MYR("MYR"),
  NZD("NZD"),
  RSD("RSD"),
  SGD("SGD"),
  THB("THB"),
  ZAR("ZAR"),
  MXN("MXN"),
  JOD("JOD"),
  VND("VND"),
  BRL("BRL"),
  MAD("MAD"),
  INR("INR"),
  PHP("PHP"),
  SAR("SAR"),
  ALL("ALL"),
  MKD("MKD"),
  MUR("MUR"),
  MDL("MDL"),
  KES("KES"),
  BAM("BAM");

  private final String currencyCode;

  ValidCurrencyEnum(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  private static final EnumSet<ValidCurrencyEnum> VALID_CURRENCIES = EnumSet.allOf(ValidCurrencyEnum.class);

  /**
   * Checks if the given currency code is valid.
   *
   * @param code The currency code to check.
   * @return true if the currency is valid, false otherwise.
   */
  public static boolean isValidCurrency(String code) {
    if (code == null) {
      return false;
    }
    return VALID_CURRENCIES.contains(code.toUpperCase());
  }
}
