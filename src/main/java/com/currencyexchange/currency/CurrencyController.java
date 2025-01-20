package com.currencyexchange.currency;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
@Tag(name = "Currency API", description = "This controller handles operations related to the list of currency names stored in the database.")
public class CurrencyController {

  private final CurrencyService currencyService;

  /**
   * Handles GET requests to retrieve a list of all available currencies.
   * This method logs the request and response, then returns the list of all currencies
   * from the database. The list is returned with a status code of 200 (OK).
   *
   * @return a {@link ResponseEntity} containing a list of currency names
   * with an HTTP status code of 200 (OK)
   */
  @Operation(
      summary = "Get all available currencies",
      description = "Retrieves a list of all available currencies from the database."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved list of currencies",
          content = @Content(mediaType = "application/json", schema = @Schema(type = "array", description = "List of currency names")))
  })
  @GetMapping
  public ResponseEntity<List<String>> getAllCurrencies() {
    log.info("Received request to get all currencies.");
    List<String> currencies = currencyService.getAllCurrencies();
    log.info("Returning list of currencies: {}", currencies);
    return ResponseEntity.ok(currencies);
  }
}
