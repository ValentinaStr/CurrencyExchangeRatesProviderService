package com.currencyexchange.controller;

import com.currencyexchange.business.CurrencyService;
import com.currencyexchange.model.Currency;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.web.bind.annotation.RequestBody;
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
                      example = "Internal server error.",
                      description = "Error message when server encounters an issue")
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
   * In case of server errors, a response with HTTP status 500 (Internal Server Error) is returned.
   *
   * @param currency      The {@link Currency} object containing the currency code to add.
   * @param bindingResult The {@link BindingResult} containing validation errors, if any.
   * @return A {@link ResponseEntity} with the result message and corresponding HTTP status.
   */
  @Operation(
      summary = "Add a new currency to the system",
      description = "Validates and adds a new currency to the system."
          + " If the currency already exists, it will be skipped. "
          + "If validation fails, a bad request response is returned.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Currency processed successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      type = "string",
                      example = "Currency processed: GBP",
                      description = "Confirmation message of the processed currency"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Validation errors found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      type = "string",
                      example = "Validation errors found",
                      description = "Error message when the currency validation fails"
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
                      description = "Error message when server encounters an issue"
                  )
              )
          )
      }
  )
  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/")
  public ResponseEntity<String> addCurrency(@Valid @RequestBody Currency currency,
                                            BindingResult bindingResult) {
    log.info("Received request to add currency: {}", currency.getCurrency());
    if (bindingResult.hasErrors()) {
      log.error("Validation failed for currency: {}", currency.getCurrency());
      return ResponseEntity.badRequest().body("Validation error : "
         +  "Currency must be 3 uppercase letters");
    }

    log.info("Validation passed for currency: {}. Adding to the system.", currency.getCurrency());
    currencyService.addCurrency(currency);
    log.info("Currency processed successfully: {}", currency.getCurrency());
    return ResponseEntity.ok("Currency processed: " + currency.getCurrency());
  }
}
