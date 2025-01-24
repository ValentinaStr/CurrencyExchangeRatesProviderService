package com.currencyexchange.enums;

import com.currencyexchange.exception.UnsupportedCurrencyException;
import java.util.Arrays;

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

  ValidCurrencyEnum(String currencyCode) {
  }

  /**
   * Validates if the given currency code exists in the list of supported currencies.
   * Throws an {@link UnsupportedCurrencyException} if the currency code is not supported.
   *
   * @param code The currency code to validate. Must not be null or empty.
   * @throws UnsupportedCurrencyException if the given currency code is not supported.
   */
  public static void isValidCurrency(String code) throws UnsupportedCurrencyException  {
    boolean isValid = Arrays.stream(ValidCurrencyEnum.values())
        .anyMatch(currency -> currency.name().equalsIgnoreCase(code));

    if (!isValid) {
      throw new UnsupportedCurrencyException("Currency " + code + " is not supported");
    }
  }
}
