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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Exchange Rates", description = "Endpoints for managing and retrieving currency exchange rates")
@RestController
public class ExchangeRateController {

  @Autowired
  private ExchangeRateCacheService exchangeRateCacheService;

  /**
   * Handles GET requests to retrieve the exchange rate for a specified currency.
   * Validates the provided currency code and retrieves the corresponding exchange rate.
   * Returns the appropriate HTTP response:
   * - If the exchange rate is found in the cache, 200 (OK).
   * - If the currency code is invalid (does not match the required format), 400 (Bad Request).
   * - If the exchange rate is not found in the cache, 404 (Not Found).
   * - If a server error occurs, 500 (Internal Server Error).
   *
   * @param currencyCode   The 3-letter currency code provided as a query parameter.
   *                       Must consist of exactly three uppercase letters (e.g., "USD", "EUR").
   *                       If invalid, a 400 (Bad Request) response is returned.
   * @return A {@code String} containing the exchange rate for the specified currency
   *         in the format: "Exchange rate for {currencyCode}: {exchangeRate}".
   * @throws IllegalArgumentException if the {@code currencyCode} does not match the required format.
   * @throws RateNotFoundInCacheException if the exchange rate cannot be found in the cache (404).
   */
  @Operation(
      summary = "Retrieve exchange rate for a specific currency",
      description = "Validates the provided currency code and retrieves the corresponding exchange rate from the cache. "
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
                      example = "Exchange rate not found for currency code: XXX",
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
  @GetMapping("/exchange-rates")
  public String getExchangeRateCached(@RequestParam("currency")
                                @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3 uppercase letters") String currencyCode) {

    Double exchangeRate = exchangeRateCacheService.getExchangeRate(currencyCode);
    return "Exchange rate for " + currencyCode + ": " + exchangeRate;
  }
}

