package com.currencyexchange.mapper;

import com.currencyexchange.dto.ExchangeratesapiClientDto;
import com.currencyexchange.dto.FixerDto;
import com.currencyexchange.model.RatesModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResponseModelMapper {
  /**
   * Converts a {@link FixerDto} object to a {@link RatesModel}.
   *
   * @param fixerDto the {@link FixerDto} object to be converted
   * @return the converted {@link RatesModel} object
   */
  RatesModel fixerDtoToRatesModel(FixerDto fixerDto);

  /**
   * Converts a {@link ExchangeratesapiClientDto} object to a {@link RatesModel}.
   *
   * @param exchangeratesDto the {@link ExchangeratesapiClientDto} object to be converted
   * @return the converted {@link RatesModel} object
   */
  RatesModel exchangeratesDtoToRatesModel(ExchangeratesapiClientDto exchangeratesDto);
}
