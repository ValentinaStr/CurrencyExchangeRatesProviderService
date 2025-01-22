package com.currencyexchange.controller;

import com.currencyexchange.business.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
   *
   * @return a {@link ResponseEntity} containing a list of currency names
   *     with an HTTP status code of 200 (OK)
   */
  @Operation(
      summary = "Get all available currencies",
      description = "Retrieves a list of all available currencies from the database."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved list of currencies",
          content = @Content(mediaType = "application/json",
              schema = @Schema(
                  type = "array",
                  example = "[\"USD\", \"EUR\", \"JPY\"]",
                  description = "List of currency codes available in the database"
              )))
  })
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public Set<String> getAllCurrencies() {
    log.info("Received request to get all currencies.");
    Set<String> currencies = currencyService.getAllCurrencies();
    log.info("Returning list of currencies: {}", currencies);
    return currencies;
  }
}
