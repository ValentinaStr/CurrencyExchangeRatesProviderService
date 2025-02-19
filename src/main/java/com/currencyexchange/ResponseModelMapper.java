package com.currencyexchange;

import com.currencyexchange.dto.ExchangeratesapiClientDto;
import com.currencyexchange.dto.FixerDto;
import com.currencyexchange.model.RatesModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResponseModelMapper {
  RatesModel fixerDtoToRatesModel(FixerDto fixerDto);

  RatesModel exchangeratesDtoToRatesModel(ExchangeratesapiClientDto exchangeratesDto);
}
