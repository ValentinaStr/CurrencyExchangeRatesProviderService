package com.currencyexchange.controller;

import com.currencyexchange.business.ExchangeRateCacheService;
import com.currencyexchange.exception.RateNotFoundInCacheException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Exchange Rates",
    description = "Endpoints for managing and retrieving currency exchange rates")
public class ExchangeRateController {

  @Autowired
  private ExchangeRateCacheService exchangeRateCacheService;

  /**
   * Endpoint that returns the exchange rate for the provided currency.
   * The currency code must be a valid 3-letter uppercase code (e.g., "USD", "GBP").
   *
   * @param currency The 3-letter currency code.
   * @return The exchange rate for the provided currency.
   * @throws RateNotFoundInCacheException
   *     If the exchange rate for the given currency is not found in the cache.
   *      Returns the corresponding HTTP status:
   *     - If the currency code is valid and found in the cache, 200 (OK).
   *     - If the currency code fails validation (not a 3-letter uppercase code), 400 (Bad Request).
   *     - If the exchange rate for the currency is not found in the cache, 404 (Not Found).
   *     - In case of server errors, 500 (Internal Server Error).
   */
  @Operation(
      summary = "Retrieve exchange rate for a specific currency",
      description = "Validates the provided currency code "
          + "and retrieves the corresponding exchange rate from the cache. "
          + "Returns appropriate HTTP responses based on the result.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Exchange rate retrieved successfully",
              content = @Content(
                  mediaType = "text/plain",
                  schema = @Schema(
                      type = "string",
                      example = "Exchange rate for USD: 1.12",
                      description = "Exchange rate information in a formatted string"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid currency code",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      type = "string",
                      example = "Currency must be 3 uppercase letters",
                      description = "Validation error message"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Exchange rate not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      type = "string",
                      example = "Exchange rate for currency : XXX not found in cache",
                      description = "Error message when the exchange rate is missing"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal Server Error",
              content = @Content(
                  mediaType = "text/plain",
                  schema = @Schema(
                      type = "string",
                      example = "Internal server error.",
                      description = "Error message when the server encounters an issue"
                  )
              )
          )
      },
      parameters = {
          @Parameter(
              name = "currency",
              description = "The 3-letter uppercase currency code (e.g., USD, EUR)",
              required = true,
              schema = @Schema(
                  type = "string",
                  example = "USD",
                  pattern = "^[A-Z]{3}$",
                  description = "Currency code consisting of exactly three uppercase letters"
              )
          )
      }
  )
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/exchange-rates/")
  public String getExchangeRateCached(@RequestParam("currency") @Pattern(regexp = "^[A-Z]{3}$",
      message = "Currency must be 3 uppercase letters")
                                      String currency) {
    log.info("Received request to get exchange rate for currency: {}", currency);
    Double exchangeRate = exchangeRateCacheService.getExchangeRate(currency);
    log.info("Exchange rate retrieved successfully for {}: {}", currency, exchangeRate);
    return "Exchange rate for " + currency + ": " + exchangeRate;
  }
}

