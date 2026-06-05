package com.currencyexchange.controller;

import com.currencyexchange.business.ExchangeRateService;
import com.currencyexchange.model.ExchangeRateModel;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ProblemDetail;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(
    name = "Exchange Rates",
    description = "Endpoints for retrieving currency exchange rates")
public class ExchangeRateController {

  private final ExchangeRateService exchangeRateService;

  /**
   * Endpoint that returns the exchange rate for the provided currency. The currency code must be a
   * valid 3-letter uppercase code (e.g., "USD", "GBP").
   *
   * @param currency The 3-letter currency code.
   * @return The exchange rate for the provided currency.
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
                    schema = @Schema(implementation = ExchangeRateModel.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid currency code",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Exchange rate not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)))
      })
  @GetMapping("/api/v1/exchange-rates")
  public ExchangeRateModel getExchangeRate(
      @Parameter(description = "3-letter uppercase currency code (e.g., USD, EUR)", example = "USD")
      @RequestParam("currency")
          @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3 uppercase letters")
          String currency) {
    log.info("Received request to get exchange rates for currency: {}", currency);
    Map<String, BigDecimal> exchangeRates = exchangeRateService.getExchangeRates(currency);
    log.info("Exchange rates retrieved successfully for {}: {}", currency, exchangeRates);
    return new ExchangeRateModel(currency, exchangeRates);
  }
}
