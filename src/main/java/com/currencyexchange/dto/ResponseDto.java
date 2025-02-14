package com.currencyexchange.dto;

import com.currencyexchange.model.Rates;

public interface ResponseDto {

  /**
   * Converts this DTO into a Rates model.
   *
   * @return a Rates instance populated with data from this DTO.
   */
  Rates toRates();
}
