package com.currencyexchange.controller;

import com.currencyexchange.business.CurrencyService;
import com.currencyexchange.exception.UnsupportedCurrencyException;
import com.currencyexchange.model.Currency;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
              description = "Internal Server Error"
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
   * Adds a currency to the database and returns the result with the corresponding HTTP status:
   * - If the currency is added, 201 (Created).
   * - If the currency already exists, 200 (OK).
   * - If the currency is invalid, 400 (Bad Request).
   *
   * @param currency The currency code to add.
   * @return A {@link ResponseEntity} with the result and HTTP status.
   */
  @Operation(
      summary = "Add a new currency",
      description = "Adds a new currency to the system by storing it in the database.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Currency successfully added",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Currency.class)
              )
          ),
          @ApiResponse(
              responseCode = "200",
              description = "Currency already exists"
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Bad Request - Invalid currency format"
          )
      }
  )
  @PostMapping("/")
  public ResponseEntity<String> addCurrency(@RequestParam String currency) {
    log.info("Received request to add new currency: {}", currency);

    try {
      if (currencyService.addCurrency(currency)) {
        log.info("Successfully added currency: {}", currency);
        return ResponseEntity.status(HttpStatus.CREATED).body("Currency added: " + currency);
      } else {
        log.info("Currency already exists: {}", currency);
        return ResponseEntity.status(HttpStatus.OK).body("Currency already exists: " + currency);
      }
    } catch (UnsupportedCurrencyException e) {
      log.error("Error adding currency: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
}
