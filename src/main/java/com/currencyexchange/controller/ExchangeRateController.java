package com.currencyexchange.controller;

import com.currencyexchange.cache.ExchangeRateCacheService;
import com.currencyexchange.dto.ExchangeRateDto;
import com.currencyexchange.exception.RateNotFoundInCacheException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(
    name = "Exchange Rates",
    description = "Endpoints for managing and retrieving currency exchange rates")
public class ExchangeRateController {

  private final ExchangeRateCacheService exchangeRateCacheService;

  /**
   * Endpoint that returns the exchange rate for the provided currency. The currency code must be a
   * valid 3-letter uppercase code (e.g., "USD", "GBP").
   *
   * @param currency The 3-letter currency code.
   * @return The exchange rate for the provided currency.
   * @throws RateNotFoundInCacheException If the exchange rate for the currency is not found.
   */
  @Operation(
      summary = "Retrieve exchange rate for a specific currency",
      description =
          "Retrieves the exchange rates for the provided currency code from the cache. "
              + "Returns a list of exchange rates for that currency.",
      security = @SecurityRequirement(name = "basicAuth"),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Exchange rate retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            type = "object",
                            example = "{\"EUR\":1.18,\"GBP\":1.0,\"USD\":1.28}",
                            description = "Map of exchange rates for the given currency"))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid currency code",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            type = "string",
                            example =
                                "{\"error\": \"Invalid currency code\","
                                    + " \"message\": \"Currency must be 3 uppercase letters\"}",
                            description = "Validation error message"))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            type = "string",
                            example =
                                "{\"error\": \"Unauthorized\","
                                    + " \"message\": \"Authentication required\"}",
                            description =
                                "Returned when the user is not authenticated "
                                    + "or credentials are invalid."))),
        @ApiResponse(
            responseCode = "404",
            description = "Exchange rate not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            type = "string",
                            example =
                                "{\"error\": \"Not found\", "
                                    + "\"message\": \"Exchange rate for currency :"
                                    + " XXX not found in cache\"}",
                            description = "Error message when the exchange rate is missing"))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content =
                @Content(
                    mediaType = "text/plain",
                    schema =
                        @Schema(
                            type = "string",
                            example =
                                "{\"error\": \"Server error\", "
                                    + "\"message\": \"Internal server error\"}",
                            description = "Error message when the server encounters an issue")))
      },
      parameters = {
        @Parameter(
            name = "currency",
            description = "The 3-letter uppercase currency code (e.g., USD, EUR)",
            required = true,
            schema =
                @Schema(
                    type = "string",
                    example = "USD",
                    pattern = "^[A-Z]{3}$",
                    description = "Currency code consisting of exactly three uppercase letters"))
      })
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/exchange-rates/")
  public ExchangeRateDto getExchangeRateCached(
      @RequestParam("currency")
          @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3 uppercase letters")
          String currency) {
    log.info("Received request to get exchange rates for currency: {}", currency);
    Map<String, BigDecimal> exchangeRates = exchangeRateCacheService.getExchangeRates(currency);
    log.info("Exchange rates retrieved successfully for {}: {}", currency, exchangeRates);
    return new ExchangeRateDto(currency, exchangeRates);
  }
}
