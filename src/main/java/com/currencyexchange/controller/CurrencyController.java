package com.currencyexchange.controller;

import com.currencyexchange.business.CurrencyService;
import com.currencyexchange.model.Currency;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
@Tag(name = "Currency API",
    description = "Handles operations for currency names stored in the database.")

public class CurrencyController {

  private final CurrencyService currencyService;

  /**
   * Handles GET requests to retrieve a list of all available currencies.
   * This method logs the request and response, then returns the list of all currencies
   * from the database. The list is returned with a status code of 200 (OK).
   * In case of server errors, a response with HTTP status 500 (Internal Server Error) is returned.
   *
   * @return a {@link ResponseEntity} containing a list of currency names
   *     with an HTTP status code of 200 (OK)
   */
  @Operation(
      summary = "Get all available currencies",
      description = "Retrieves a list of all available currencies from the database.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Successful Operation",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      type = "array",
                      example = "[\"USD\", \"EUR\", \"JPY\"]",
                      description = "List of currency codes available in the database"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal Server Error ",
              content = @Content(
                  mediaType = "text/plain",
                  schema = @Schema(type = "string",
                      example = "Internal server error.")
              )
          )
      }
  )
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/")
  public Set<String> getAllCurrencies() {
    log.info("Received request to get all currencies.");
    Set<String> currencies = currencyService.getAllCurrencies();
    log.info("Returning list of currencies: {}", currencies);
    return currencies;
  }

  /**
   * Handles POST requests to add a new currency to the system.
   * Validates the provided currency code and stores it in the database.
   * Returns the corresponding HTTP status:
   * - If the currency is added or already exists, 200 (OK).
   * - If the currency fails validation, 400 (Bad Request).
   *
   * @param currency      The {@link Currency} object containing the currency code to add.
   * @param bindingResult The {@link BindingResult} containing validation errors, if any.
   * @return A {@link ResponseEntity} with the result message and corresponding HTTP status.
   */
  @Operation(
      summary = "Add a new currency",
      description = "Validates and adds a new currency to the system "
         +  "by storing it in the database.",
      requestBody = @RequestBody(
          description = "Validates and adds a new currency by storing it in the database."
            +  "The currency code must be a valid 3-letter uppercase code (e.g., 'USD')",
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = Currency.class)
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Currency successfully added or already exists",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(type = "string", example = "Currency registered: USD")
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Currency validation failed",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(type = "string", example = "Currency validation failed")
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error occurred while processing the request",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(type = "string", example = "Internal server error")
              )
          )
      }
  )
  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/")
  public ResponseEntity<String> addCurrency(@RequestBody @Valid Currency currency, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().body("Validation errors found");
    }
    currencyService.addCurrency(currency);
    return ResponseEntity.ok("Currency processed: " + currency.getCurrency());
  }
}
